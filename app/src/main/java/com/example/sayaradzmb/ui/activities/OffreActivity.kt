package com.example.sayaradzmb.ui.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.Automobiliste
import com.example.sayaradzmb.model.VehiculeOccasion
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_offre.*
import kotlinx.android.synthetic.main.activity_offre.toolbar
import kotlinx.android.synthetic.main.ajouter_annonce_activity.*
import kotlinx.android.synthetic.main.content_offre.*
import kotlinx.android.synthetic.main.main_card_view.*
import kotlinx.android.synthetic.main.main_card_view.car_title

class OffreActivity : AppCompatActivity(), SharedPreferenceInterface {

    private var annonce : VehiculeOccasion? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offre)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp)

        //pour retourner vers l'activité précédente
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.title = "Offre"

        //get the intent
        val  intent = intent
        if (intent.hasExtra("annonce"))
        {
            annonce = intent.getParcelableExtra("annonce")
            Toast.makeText(this,annonce.toString(), Toast.LENGTH_LONG).show()
            setInfo(annonce!!)
        }
        else {
            Log.e("intent", "You can't get the data you are looking for")
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)

        val menuItem = menu!!.findItem(R.id.icon_notification)

        val actionView = MenuItemCompat.getActionView(menuItem)

        actionView.setOnClickListener { onOptionsItemSelected(menuItem) }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setInfo(annonceInfo : VehiculeOccasion){

        //Car information
        car_title.text = annonceInfo.version!!.NomMarque.plus(" ")
            .plus(annonceInfo.version!!.NomModele).plus(" ")
            .plus(annonceInfo.version!!.NomVersion)

        car_desc.text = annonceInfo.Description
        car_price.text = annonceInfo.Prix.plus("DZA")

        Picasso.get()
            .load(annonceInfo.images!![0].CheminImage)
            .centerCrop()
            .fit()
            .into(car_img)


        //Seller info
        seller_name.text = annonceInfo.automobiliste!!.Nom.plus(" ").plus(annonceInfo.automobiliste!!.Prenom)
        seller_mail.text = "seller@email.dz"
        seller_number.text = annonceInfo.automobiliste!!.NumTel

        //Owner info
        //first get infos
        val user : Automobiliste = avoirInfoUser(this)
        owner_name.text = user.Nom.plus(" ").plus(user.Prenom)
        owner_mail.text = "owner@email.dz"
        owner_number.text = user.NumTel

    }


}
