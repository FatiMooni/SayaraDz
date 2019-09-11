package com.example.sayaradzmb.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.Annonce
import com.example.sayaradzmb.model.Automobiliste
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.ui.adapter.VehiculeImageAdapter
import com.example.sayaradzmb.viewmodel.AnnonceViewModel
import kotlinx.android.synthetic.main.activity_ajouter_annonce.*

class AnnonceApercuActivity : AppCompatActivity(), SharedPreferenceInterface {
    /**
     *
     */
    private var idUser: String? = null
    private var pAdapter: VehiculeImageAdapter? = null
    private var annonce: VehiculeOccasion? = null

    companion object {
        const val EXTRA_ANNOUNCE = "annonce"
        const val EXTRA_ANNONCE_ID = "annonceId"
        const val EXTRA_AUTOMOBILISTE = "automobiliste"
        const val EXTRA_ANNONCE_NAME = "annonceVersion"
    }

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

        val intent = intent
        when {
            intent.hasExtra(EXTRA_ANNOUNCE) -> {
                annonce = intent.getParcelableExtra(EXTRA_ANNOUNCE)
                Toast.makeText(this, annonce.toString(), Toast.LENGTH_LONG).show()
                setData(annonce)
            }
            intent.hasExtra(EXTRA_ANNONCE_ID) && intent.hasExtra(EXTRA_AUTOMOBILISTE) -> {
                var annonce : Annonce? = null
                val id = intent.getIntExtra(EXTRA_ANNONCE_ID, -1)
                val automobile = intent.getParcelableExtra<Automobiliste>(EXTRA_AUTOMOBILISTE)
                val version = intent.getStringExtra(EXTRA_ANNONCE_NAME)
                val model : AnnonceViewModel = ViewModelProviders.of(this).get(AnnonceViewModel::class.java)
                model.getAnnonce().observe(this, Observer<Annonce> {
                    annonce = it!!
                    if (annonce != null) setData(annonce , automobile, version)
                    Toast.makeText(this, annonce.toString(), Toast.LENGTH_LONG).show()
                })
                model.loadAnnounce(id)
            }
            else -> Log.e("intent", "You can't get the data you are looking for")
        }


    }

    private fun setData(annonce: Annonce?, automobiliste: Automobiliste , version : String) {
        //Owner informations
        findViewById<TextView>(R.id.nom_owner).text = automobiliste.Nom
            .plus(" "+automobiliste.Prenom)
        findViewById<TextView>(R.id.owner_mail).text = "07 85 xx xx xx"
        //init the adapter
        pAdapter = VehiculeImageAdapter(this, annonce!!.images!!)

        //Setting the data
        findViewById<TextView>(R.id.marque_title).text = version
        findViewById<TextView>(R.id.prix_specification).text = annonce.Prix
        findViewById<TextView>(R.id.KM_title).text = annonce.Km
        findViewById<TextView>(R.id.color_title).text = annonce.Couleur
        findViewById<TextView>(R.id.year_title).text = annonce.Annee.toString()
        findViewById<TextView>(R.id.type_title).text = annonce.Carburant
        findViewById<TextView>(R.id.text_description).text = annonce.Description
        findViewById<ViewPager>(R.id.images_viewer).adapter = pAdapter
    }


    private fun setData(annonce: VehiculeOccasion?) {
        //Owner informations
        findViewById<TextView>(R.id.nom_owner).text = annonce!!.automobiliste!!.Nom.plus(" ")
            .plus(annonce.automobiliste!!.Prenom)
        findViewById<TextView>(R.id.owner_mail).text = "07 85 xx xx xx"
        //init the adapter
        pAdapter = VehiculeImageAdapter(this, annonce.images!!)

        //Setting the data
        findViewById<TextView>(R.id.marque_title).text = annonce.version!!.NomMarque
        findViewById<TextView>(R.id.prix_specification).text = annonce.Prix
        findViewById<TextView>(R.id.KM_title).text = annonce.Km
        findViewById<TextView>(R.id.color_title).text = annonce.Couleur
        findViewById<TextView>(R.id.year_title).text = annonce.Annee.toString()
        findViewById<TextView>(R.id.type_title).text = annonce.Carburant
        findViewById<TextView>(R.id.text_description).text = annonce.Description
        findViewById<ViewPager>(R.id.images_viewer).adapter = pAdapter
    }

}