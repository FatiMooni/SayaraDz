package com.example.sayaradzmb.ui.activities


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferencesHelper
import com.example.sayaradzmb.model.Version
import com.example.sayaradzmb.ui.activities.fragments.*
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_accuille.*
import kotlinx.android.synthetic.main.toolbar_accuille.*


@Suppress("CAST_NEVER_SUCCEEDS")
class AcuilleActivity : AppCompatActivity(), NouveauRechercheCars.OnSearchPressed,
    NavigationView.OnNavigationItemSelectedListener,
    NouveauAfficheTechnique.OnCommandPressed {


    private var pref: SharedPreferencesHelper? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    var textCartItemCount: TextView? = null
    var mCartItemCount = 10


    /**
     * la focntion qui aide a switche entre les fragment
     */

    override fun envoyerFragment(int: Int) {
        var fragment: Fragment? = null
        when (int) {
            2 -> {
                fragment = NouveauCommandeFragment()
                titre_fonction.text = getString(R.string.commander_acc)

            }
        }
        chargerFragment(fragment)
    }

    override fun envoyerFragment(int: Int, version: Version) {
        var fragment: Fragment? = null
        when (int) {
            1 -> {
                fragment = NouveauAfficheTechnique(version)
                titre_fonction.text = getString(R.string.fiche_acc)

            }
        }
        chargerFragment(fragment)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_menu)
        navigationTest()
        val toolbar: Toolbar = findViewById(R.id.acc_toolbar)
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
       // TODO("no user fetched")
        header.findViewById<TextView>(R.id.UserName).text = "Abdiche Fatima Zahra"
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        //////
        chargerFragment(NouveauRechercheCars())

    }


    /**
     * Pour Deconnecter
     */
    private fun deconnecter() {
        val login = Intent(this@AcuilleActivity, LoginActivity::class.java)
        startActivity(login)
        this@AcuilleActivity.finish()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)

        val menuItem = menu!!.findItem(R.id.icon_notification)

        val actionView = MenuItemCompat.getActionView(menuItem)
        textCartItemCount = actionView.findViewById(R.id.cart_badge) as TextView

        setupBadge()

        actionView.setOnClickListener { onOptionsItemSelected(menuItem) }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupBadge() {

        if (textCartItemCount != null) {
            if (0 == mCartItemCount) {
                if (textCartItemCount!!.visibility != View.GONE) {
                    textCartItemCount!!.visibility = View.GONE
                }
            } else {
                textCartItemCount!!.text = "12"
                if (textCartItemCount!!.visibility != View.VISIBLE) {
                    textCartItemCount!!.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.deconnecter -> {
                facebookDisconnection()
                googleDisconnection()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * deconnection Facebook
     */
    private fun facebookDisconnection() {
        LoginManager.getInstance().logOut()
        AccessToken.setCurrentAccessToken(null)
        pref = SharedPreferencesHelper(this@AcuilleActivity, "facebook")
        pref!!.sharedPreferences.edit().clear().apply()
        deconnecter()
    }

    /**
     * deconnection Google
     */
    private fun googleDisconnection() {
        //signIn out from the the google account
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {
                pref = sharedPref(this@AcuilleActivity, "google")
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
    private fun sharedPref(context: Context, nomFichier: String): SharedPreferencesHelper {
        return SharedPreferencesHelper(context, nomFichier)
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
    private fun chargerFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_id, fragment).commit()
            return true
        }
        return false
    }

    /**
     * les tache a faire a partire de bottomNavigation View
     */
    private fun navigationTest() {
        navigation_bar.setOnNavigationItemSelectedListener {
            var fragment: Fragment? = null
            val titre = findViewById<TextView>(R.id.titre_fonction)
            header_holder.background = this.resources.getDrawable(R.drawable.header, null)
            when (it.itemId) {
                R.id.nouelle_voiture -> {
                    fragment = NouveauRechercheCars()
                    titre.text = getString(R.string.rech_acc)

                }
                R.id.occasion_voiture -> {
                    fragment = OccasionFragment()
                    titre.text = getString(R.string.occ_acc)

                }
                R.id.accuuille_voiture -> {
                    fragment = AccuilleFragment()
                    header_holder.background = this.resources.getDrawable(R.drawable.background_acc, null)
                    titre.text = getString(R.string.acc)

                }
                R.id.annoce_voiture -> {
                    fragment = AnnonceFragment()
                    titre.text = getString(R.string.announce_acc)


                }
                R.id.favoris_voiture -> {
                    fragment = FavorisFragment()
                    titre.text = getString(R.string.fav_acc)
                }

            }
            return@setOnNavigationItemSelectedListener chargerFragment(fragment)
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
                googleDisconnection()
                deconnecter()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}


