package com.example.sayaradzmb.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent

import android.os.Bundle;
import android.support.design.widget.NavigationView

import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar
import android.view.Menu


import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.example.sayaradzmb.R
import com.example.sayaradzmb.activities.fragments.OccasionFragment
import kotlinx.android.synthetic.main.activity_accuille.*

import com.example.sayaradzmb.helper.SharedPreferencesHelper
import com.example.sayaradzmb.model.Version
import com.example.sayaradzmb.ui.activities.fragments.*
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.toolbar_accuille.*


@Suppress("CAST_NEVER_SUCCEEDS")
class AcuilleActivity : AppCompatActivity(), NouveauRechercheCars.OnSearchPressed,NavigationView.OnNavigationItemSelectedListener,
    NouveauAfficheTechnique.OnCommandPressed{


    private var pref: SharedPreferencesHelper? = null
    private var mGoogleSignInClient : GoogleSignInClient? = null
    var textCartItemCount: TextView? = null
    var mCartItemCount = 10





    /**
     * la focntion qui aide a switche entre les fragment
     */

    override fun envoyerFragment(int: Int) {
        var fragment : Fragment?=null
        when(int){
            2->{
                fragment= NouveauCommandeFragment()
                titre_fonction.text = "Commander une Voiture"

            }
        }
        chargerFagment(fragment)
    }

    override fun envoyerFragment(int : Int,version: Version) {
        var fragment : Fragment?=null
        when(int){
            1->{
                fragment= NouveauAfficheTechnique(version)
                titre_fonction.text = "Fiche Technique"

            }
        }
        chargerFagment(fragment)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_menu)
        navigationTest()
        val toolbar : Toolbar = findViewById(R.id.acc_toolbar)
        setSupportActionBar(toolbar)


        /*
        adding a drawer ( side navigation menu )
         */
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open, R.string.app_name
        )

        val header = navView.inflateHeaderView(R.layout.nav_header_side_menu)
        header.findViewById<TextView>(R.id.UserName).text = "Abdiche Fatima Zahra"
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        //////
        chargerFagment(NouveauRechercheCars())

    }



    /**
     * Pour Deconnecter
     */
    private fun deconnecter(){
            val login = Intent(this@AcuilleActivity, LoginActivity::class.java)
            startActivity(login)
            this@AcuilleActivity.finish()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu,menu)

        val menuItem = menu!!.findItem(R.id.icon_notification)

        val actionView = MenuItemCompat.getActionView(menuItem)
        textCartItemCount = actionView.findViewById(R.id.cart_badge) as TextView

        setupBadge()

        actionView.setOnClickListener { onOptionsItemSelected(menuItem) }
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
        pref = SharedPreferencesHelper(this@AcuilleActivity,"facebook")
        pref!!.sharedPreferences.edit().clear().apply()
        deconnecter()
    }

    /**
     * deconnection Google
     */
    private fun googleDeconnexion(){
        //signIn out from the the google account
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {
                pref = sharedPref(this@AcuilleActivity,"google")
                pref!!.sharedPreferences.edit().clear().apply()
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
                    titre.text="Recherche"

                }
                R.id.occasion_voiture -> {
                    fragment = OccasionFragment()
                    titre.text="Véhicules Occasions"

                }
                R.id.accuuille_voiture -> {
                    fragment = AccuilleFragment()
                    titre.text="Acceuille"

                }
                R.id.annoce_voiture -> {
                    fragment = AnnonceFragment()
                    titre.text="Mes Annonces"

                }
                R.id.favoris_voiture -> {
                    fragment = FavorisFragment()
                    titre.text="Favoris"
                }

            }
            return@setOnNavigationItemSelectedListener chargerFagment(fragment)
        }

    }


    /**
     * for the side navigation menu
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                // Handle the camera action
                Toast.makeText(this, "profile", Toast.LENGTH_LONG).show()
            }
            R.id.nav_offer -> {

            }
            R.id.nav_command -> {

            }
            R.id.nav_following -> {

            }
            R.id.nav_favoris -> {

            }
            R.id.nav_support -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_us -> {

            }
            R.id.website_link -> {

            }
            R.id.deconnect -> {
                googleDeconnexion()
                deconnecter()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true    }

}

