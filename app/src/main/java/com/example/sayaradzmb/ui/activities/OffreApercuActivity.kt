package com.example.sayaradzmb.ui.activities

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.ui.adapter.VehiculeImageAdapter
import kotlinx.android.synthetic.main.ajouter_annonce_activity.*

class OffreApercuActivity :  AppCompatActivity() , SharedPreferenceInterface {
    /**
     *
     */
    private var idUser : String? = null
    private var pAdapter : VehiculeImageAdapter? = null
    private var annonce : VehiculeOccasion? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idUser = avoirIdUser(this).toString()
        setContentView(R.layout.activity_annonce_apercu)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp)

        //pour retourner vers l'activité précédente
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val  intent = intent
        if (intent.hasExtra("annonce"))
        {
            annonce = intent.getParcelableExtra("annonce")
            Toast.makeText(this,annonce.toString(),Toast.LENGTH_LONG).show()
        }
        else {
            Log.e("intent", "You can't get the data you are looking for")
        }

        //Owner informations
        findViewById<TextView>(R.id.nom_owner).text = annonce!!.automobiliste!!.Nom.plus(" ")
                                                     .plus( annonce!!.automobiliste!!.Prenom)
        findViewById<TextView>(R.id.owner_mail).text = "07 85 xx xx xx"
        //init the adapter
        pAdapter = VehiculeImageAdapter(this, annonce!!.images!!)

        //Setting the data
        findViewById<TextView>(R.id.marque_title).text = annonce!!.version!!.NomMarque
        findViewById<TextView>(R.id.prix_specification).text = annonce!!.Prix
        findViewById<TextView>(R.id.KM_title).text = annonce!!.Km
        findViewById<TextView>(R.id.color_title).text = annonce!!.Couleur
        findViewById<TextView>(R.id.year_title).text = "jan 2018"
        findViewById<TextView>(R.id.type_title).text = annonce!!.Carburant
        findViewById<TextView>(R.id.text_description).text = annonce!!.Description
        findViewById<ViewPager>(R.id.images_viewer).adapter = pAdapter


    }




}