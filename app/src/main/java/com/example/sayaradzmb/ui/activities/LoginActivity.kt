package com.example.sayaradzmb.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*
import com.facebook.login.LoginResult
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Base64
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.constatnte.*
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.Automobiliste
import com.example.sayaradzmb.servics.AuthService
import com.example.sayaradzmb.servics.ServiceBuilder
import com.facebook.*
import com.facebook.GraphRequest.newMeRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * avoir le client id de string.xml
 */
const val googleToken = "493881959162-0ccs4e44m4gr9e576crna4gcm659ah5d.apps.googleusercontent.com"
class LoginActivity : AppCompatActivity(), SharedPreferenceInterface {

    var callbackManager : CallbackManager? =null
    @SuppressLint("PackageMana gerGetSignatures")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //pauseActivity()
        googleAuth()
        dejaConnecteFacebook()



    }// fin de OnCreat

    /**la fonction quimet un temps pour faire transition
     *
     */
    private fun pauseActivity(){
        val handler = Handler()
        handler.postDelayed({
            // do something
            val intent = Intent(this@LoginActivity, AcuilleActivity::class.java)
            // If you just use this that is not a valid context. Use ActivityName.this
            startActivity(intent)
            this@LoginActivity.finish()
        }, 3000)
    }
    /**
     * test the next connection facebook
     */
    public fun dejaConnecteFacebook(){
        if(AccessToken.getCurrentAccessToken() == null) {
            val call = facebookInit()
            if (call != null) {
                facebookAuth(call)
            }
        }else{
            dejaConnecte()
        }
    }


    /**
     *
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            super.onActivityResult(requestCode, resultCode, data)
            println("sign 123")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }else{
            super.onActivityResult(requestCode, resultCode, data)
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    } //fin onActivityResult

    /**
     * sign in of google called from onActivity result
     */
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            if (account != null) {
                connxionSuccefulGoogle(account)
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            connexionFailedGoogle(e)
        }

    } //fin handleSignInResult

    /**
     *
     */
    fun connxionSuccefulGoogle(account : GoogleSignInAccount){
        // la communication
        val token = account.idToken

        /**
         * avoir id prenom nom
         */
        saveInfoGoogle(account)
        val automobiliste = Automobiliste(account.id!!,account.givenName!!,account.familyName!!,null)

        /**
         * la requete de retrofit
         */

        val authService = ServiceBuilder.buildService(AuthService::class.java)
        val requestCall = authService.setToken("${NOM_INIT_AUTH} ${LETTRE_GOOGLE_AUTH} ${token}",automobiliste)
        requestCall.enqueue(object : Callback<Automobiliste> {
            override fun onResponse(call: Call<Automobiliste>, response: Response<Automobiliste>) {
                if(response.isSuccessful){
                    dejaConnecte()
                }else{
                    Toast.makeText(this@LoginActivity,"Failed to connect",Toast.LENGTH_LONG)
                }

            }
            override fun onFailure(call: Call<Automobiliste>, t: Throwable) {
                Toast.makeText(this@LoginActivity,"Failed",Toast.LENGTH_LONG)
            }
        })





    } // fin connxionSuccefulGoogle

    /**
     *
     */
    fun connexionFailedGoogle(e : ApiException){
        Log.w("tag", "signInResult:failed code=" + e.printStackTrace())
        if(e.statusCode == 12500) {
            println(12500)
         Toast.makeText(this@LoginActivity,"Update your Google play Account",Toast.LENGTH_LONG)
    }
        googleButtonVisible()
        facebookButtonVisible()
    }// fin connexionFailedGoogle

    /**
     *
     */
    private fun googleAuth(){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(googleToken)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            println("account google ${account.toString()}")
            saveInfoGoogle(account)
            dejaConnecte()
        }
        google_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


    }

    /******************************************************
     *
     *                      Facebook
     *
     ******************************************************/
    /**
     *
     */
    fun connexionSuccessFace(loginResult : LoginResult){
        val token =loginResult.accessToken
        loginInformationUtilisateur(token)
        val authService = ServiceBuilder.buildService(AuthService::class.java)
        /**
         *
         */
        val automobiliste = avoirInfoUser(this@LoginActivity)
        /**
         *
         */
        val requestCall = authService.setToken("${NOM_INIT_AUTH} ${LETTRE_FACEBOOK_AUTH} ${token.token}",automobiliste)

        requestCall.enqueue(object : Callback<Automobiliste> {
            override fun onResponse(call: Call<Automobiliste>, response: Response<Automobiliste>) {
                if(response.isSuccessful){
                    dejaConnecte()

                }else{
                    Toast.makeText(this@LoginActivity,"Failed to connect",Toast.LENGTH_LONG)
                    Log.w("response !success","la connexion echouee"+response)
                }

            }
            override fun onFailure(call: Call<Automobiliste>, t: Throwable) {
                Toast.makeText(this@LoginActivity,"Failed",Toast.LENGTH_LONG).show()
                Log.w("facebook failure",t.message)
            }
        })
    }

    /**
     *
     */
    fun connexionCancelFace(){
        googleButtonVisible()
        facebookButtonVisible()
    }

    /**
     *
     */
    fun connexionErrorFace(exception : FacebookException){

        Log.w("pb cnx", ""+ exception.message)
        googleButtonVisible()
        facebookButtonVisible()
    }

    /**
     *
     */
    fun googleButtonVisible(){
        google_button.visibility =View.VISIBLE
    }

    /**
     *
     */
    fun googleButtonGone(){
        google_button.visibility = View.GONE
    }

    /**
     *
     */
    fun facebookButtonVisible(){
        facebook_button.visibility=View.VISIBLE
    }

    /**
     *
     */
    fun facebookButtonGone(){
        facebook_button.visibility =View.GONE
    }

    /**
     * la fonction pour avoir le hash key de facebook
     */
    private fun avoirHashKeyFb(){
        var any = try {
            val info = packageManager.getPackageInfo("com.example.sayaradzmb", PackageManager.GET_SIGNATURES);
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("KeyHash:", e.toString());
        } catch (e: NoSuchAlgorithmException) {
            Log.e("KeyHash:", e.toString());
        }
    }

    /**
     * la fonctionde la connexion facebook
     */
    private fun facebookAuth(callbackManager: CallbackManager){
        facebook_button.setOnClickListener {

            Toast.makeText(this@LoginActivity,"Try to connect",Toast.LENGTH_LONG).show()
            login_button.performClick()
            login_button.registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        // App code
                        connexionSuccessFace(loginResult)
                    }
                    override fun onCancel() {
                        // App code
                        connexionCancelFace()
                    }
                    override fun onError(exception: FacebookException) {
                        // App code
                        connexionErrorFace(exception)
                    }
                })

        }
        //Pour répondre au résultat d’une demande de connexion, vous devez enregistrer un rappel soit avec LoginManager,
        // soit avec LoginButton. Si vous enregistrez le rappel avec LoginButton, vous n’avez pas besoin d’enregistrer
        // le rappel dans le gestionnaire de connexion.
    }

    /**
     * l'initialisation des donnee qui sont necessaire pour la connexion facebook
     */
    private fun facebookInit(): CallbackManager? {
        callbackManager = CallbackManager.Factory.create()
      /*  login_button.setPermissions(Arrays.asList(
            "public_profile",
            "email"
        ))*/
        return callbackManager
    }

    /**
     * elle permet de stocker les information de login facebook dans prefs
     */
    private fun loginInformationUtilisateur(token : AccessToken){
        val requete = newMeRequest(token) { jsonObject, response ->
            if (jsonObject != null) {
                println("json $jsonObject")
                val id = jsonObject.getString("id")
                val listnom = jsonObject.getString("name").split(' ')
                val nom = listnom[0]
                val prenom = listnom[1]
                saveInfoFacebook(id,prenom,nom)
            }
        }
        val parametre = Bundle()
        parametre.putString("feilds","email,first_name")
        requete.parameters=parametre
        requete.executeAsync()

    }

    /**
     * la fonction qui permet de passer a Accuile Activity
     *
     */
    private fun dejaConnecte(){
        val accuille = Intent(this@LoginActivity, AcuilleActivity::class.java)
        startActivity(accuille)
        this@LoginActivity.finish()
    }

    private fun saveInfoGoogle(account: GoogleSignInAccount) {
        val pref = sharedPref(this@LoginActivity, NOM_FICHER_LOGIN)
        pref.setLoginDetails(account.id!!, account.givenName!!, account.familyName!!)
    }

    private fun saveInfoFacebook(id:String,nom:String,prenom:String) {
        val pref = sharedPref(this@LoginActivity, NOM_FICHER_LOGIN)
        pref.setLoginDetails(id, prenom, nom)
    }






}


