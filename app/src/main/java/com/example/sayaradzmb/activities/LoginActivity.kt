package com.example.sayaradzmb.activities

import android.annotation.SuppressLint
import android.content.Context
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

import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Base64
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferencesHelper
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
import java.util.*


const val RC_SIGN_IN=123
const val googleToken = "213982533320-l5lfe2acv2uoqgb8j01oceqqsbvthor7.apps.googleusercontent.com"
const val Cleint_Scret = " tbkhEAa1cL4qj7ngUvZTdmDx "
class LoginActivity : AppCompatActivity() {

    var callbackManager : CallbackManager? =null
    @SuppressLint("PackageManagerGetSignatures")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     val handler = Handler()
        handler.postDelayed({
            // do something
            val intent = Intent(this@LoginActivity, AjouterAnnonceActivity::class.java)
            // If you just use this that is not a valid context. Use ActivityName.this
            startActivity(intent)
        }, 30)



/*
         Google Authentification

          */
        setContentView(R.layout.activity_login)
        //pauseActivity()
        //avoirHashKeyFb()
        //googleAutetiicato

        pauseActivity()
        //googleAutetiicaton

        googleAuth()
        //facebookAuthentification
        // avoir une facon de gerer la prochaine fois que l'utilisateur est connecte
        if(AccessToken.getCurrentAccessToken() == null) {
            val call = facebookInit()
            if (call != null) {
                facebookAuth(call)
            }
        }else{
            dejaConnecte()
        }


    }// fin de OnCreat


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            super.onActivityResult(requestCode, resultCode, data)
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            println("un autre pb singin")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }else{
            super.onActivityResult(requestCode, resultCode, data)
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    } //fin onActivityResult

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


    fun connxionSuccefulGoogle(account : GoogleSignInAccount){
        // la communication
        val token = account.idToken
        val pref = sharedPref(this@LoginActivity,"google")
        pref.setLoginDetails(account.id!!, account.givenName!!, account.familyName!!)

        val automobiliste = Automobiliste(account.id!!,account.givenName!!,account.familyName!!,null)

        val authService = ServiceBuilder.buildService(AuthService::class.java)
        val requestCall = authService.setToken("Bearer G ${token}",automobiliste)
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
    fun connexionFailedGoogle(e : ApiException){
        Log.w("tag", "signInResult:failed code=" + e.statusCode)
        if(e.statusCode == 12500) {
            println(12500)
         Toast.makeText(this@LoginActivity,"Update your Google play Account",Toast.LENGTH_LONG)
    }
        googleButtonVisible()
        facebookButtonVisible()
    }// fin connexionFailedGoogle


    fun connexionSuccessFace(loginResult : LoginResult){
        val token =loginResult.accessToken
        loginInformationUtilisateur(token)
        val authService = ServiceBuilder.buildService(AuthService::class.java)
        val pref = SharedPreferencesHelper(this@LoginActivity,"facebook")
        val nom = pref.sharedPreferences.getString("userNom",null)
        val prenom =pref.sharedPreferences.getString("userPrenom",null)
        val requestCall = authService.setToken("Bearer F ${token.token}",Automobiliste(loginResult.accessToken?.userId!!,prenom,nom,null))
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
    }


    fun connexionCancelFace(){

        googleButtonVisible()
        facebookButtonVisible()}

    fun connexionErrorFace(exception : FacebookException){

        Log.w("pb cnx", ""+ exception.message)
        googleButtonVisible()
        facebookButtonVisible()
    }




    fun googleButtonVisible(){
        google_button.visibility =View.VISIBLE
    }
    fun googleButtonGone(){
        google_button.visibility = View.GONE
    }
    fun facebookButtonVisible(){
        facebook_button.visibility=View.VISIBLE
    }
    fun facebookButtonGone(){
        facebook_button.visibility =View.GONE
    }


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
            val pref = sharedPref(this@LoginActivity,"google")
            pref.setLoginDetails(account.id!!, account.givenName!!, account.familyName!!)
            dejaConnecte()
        }
        google_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


    }

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
        }, 3000)
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

            Toast.makeText(this@LoginActivity,"Try to connect",Toast.LENGTH_LONG)
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
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
        callbackManager = CallbackManager.Factory.create();
        login_button.setReadPermissions(Arrays.asList(
            "public_profile",
            "email"
        ))
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
                val nom = jsonObject.getString("name")
                val prenom = "surname"
                val prefs = SharedPreferencesHelper(this@LoginActivity,"facebook")
                prefs.setLoginDetails(id,nom,prenom)
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
        val accuille = Intent(this@LoginActivity,AcuilleActivity::class.java)
        startActivity(accuille)
        this@LoginActivity.finish()
    }

    /**
     * retourne une instance de ShaeredPrefernceHelper en entrant le nom de Fichier
     */
    private fun sharedPref(context:Context,nomFichier : String) : SharedPreferencesHelper{
        return SharedPreferencesHelper(context,nomFichier)
    }

}


