package com.example.sayaradzmb.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent

import android.os.Bundle;

import android.support.v4.app.Fragment

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar
import android.view.Menu


import android.view.MenuItem
import android.widget.TextView

import com.example.sayaradzmb.R
import kotlinx.android.synthetic.main.activity_accuille.*

import com.example.sayaradzmb.activities.Fragments.*
import com.example.sayaradzmb.helper.SharedPreferencesHelper
import com.example.sayaradzmb.model.version
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.support.v4.view.MenuItemCompat
import android.view.View
import com.example.sayaradzmb.ui.activities.Fragments.NouveauAfficheTechnique


@Suppress("CAST_NEVER_SUCCEEDS")
class AcuilleActivity : AppCompatActivity(),NouveauRechercheCars.OnSearchPressed, NouveauAfficheTechnique.OnCommandPressed{


    private var mGoogleSignInClient : GoogleSignInClient? = null
    var textCartItemCount: TextView? = null
    var mCartItemCount = 10



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accuille)
        navigationTest()
        setSupportActionBar(acc_toolbar as Toolbar)
        (acc_toolbar as Toolbar).setNavigationIcon(R.drawable.menu_icon)
        chargerFagment(NouveauRechercheCars())
    }


    /**
     * la focntion qui aide a switche entre les fragment
     */
    override fun envoyerFragment(int: Int) {
        var fragment : Fragment?=null
        when(int){
            2->{
                fragment=NouveauCommandeFragment()
            }
        }
        chargerFagment(fragment)
    }

    override fun envoyerFragment(int : Int,version: version) {
        var fragment : Fragment?=null
        when(int){
            1->{
                fragment= NouveauAfficheTechnique(version)

            }
        }
        chargerFagment(fragment)
    }
    /**
     * Pour Deconnecter
     */
    private fun deconnecter(){
            val login = Intent(this@AcuilleActivity,LoginActivity::class.java)
            startActivity(login)
            this@AcuilleActivity.finish()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu,menu)

        val menuItem = menu!!.findItem(R.id.icon_notification)

        val actionView = MenuItemCompat.getActionView(menuItem)
        textCartItemCount = actionView.findViewById(com.example.sayaradzmb.R.id.cart_badge) as TextView

        setupBadge()

        actionView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                onOptionsItemSelected(menuItem)
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount === 0) {
                if (textCartItemCount!!.visibility !== View.GONE) {
                    textCartItemCount!!.visibility = View.GONE
                }
            } else {
                textCartItemCount!!.text = "12"
                if (textCartItemCount!!.visibility !== View.VISIBLE) {
                    textCartItemCount!!.visibility = View.VISIBLE
                }
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){
            R.id.deconnecter -> {
                facebookDeconnexion()
                googleDeconnexion()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * deconnection Facebook
     */
    private fun facebookDeconnexion(){
        LoginManager.getInstance().logOut()
        AccessToken.setCurrentAccessToken(null)
        val pref = SharedPreferencesHelper(this@AcuilleActivity,"facebook")
        pref.sharedPreferences.edit().clear().apply()
        deconnecter()
    }

    /**
     * deconnection Google
     */
    private fun googleDeconnexion(){
        //signIn out from the the google account
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {
                val pref = sharedPref(this@AcuilleActivity,"google")
                pref.sharedPreferences.edit().clear().apply()
                deconnecter()
            }

        // revokeAccess
        mGoogleSignInClient!!.revokeAccess()
            .addOnCompleteListener(this) {
                // ...
            }
    }

    /**
     * creat sharedPreferenceHelper
     */
    private fun sharedPref(context: Context, nomFichier : String) : SharedPreferencesHelper {
        return SharedPreferencesHelper(context,nomFichier)
    }

    /**
     * on start activity
     */
    override fun onStart() {
        super.onStart()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(googleToken)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }


    /**
     * le changement de fragement
     */
    @SuppressLint("CommitTransaction")
    private fun chargerFagment(fragment: Fragment?):Boolean{
        if(fragment != null){
            supportFragmentManager.beginTransaction().replace(R.id.fragment_id,fragment).commit()
            return true
        }
        return false
    }

    /**
     * les tache a faire a partire de bottomNavigation View
     */
    private fun navigationTest(){
        navigation_bar.setOnNavigationItemSelectedListener{
            var fragment: Fragment? = null
            var titre = findViewById<TextView>(R.id.titre_fonction)
            when (it.itemId) {
                R.id.nouelle_voiture -> {
                    fragment = NouveauRechercheCars()
                    titre.text="Nouvelle Voiture"
                }
                R.id.occasion_voiture -> {
                    fragment = OccasionFragment()
                    titre.text="Occasion Voiture"
                }
                R.id.accuuille_voiture -> {
                    fragment = AccuilleFragment()
                    titre.text="Accuille"
                }
                R.id.annoce_voiture -> {
                    fragment = AnnonceFragment()

                }
                R.id.favoris_voiture -> {
                    fragment = FavorisFragment()
                    titre.text="Favoris"
                }

            }
            return@setOnNavigationItemSelectedListener chargerFagment(fragment)
        }
    }
}


