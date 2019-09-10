package com.example.sayaradzmb.ui.adapter.viewholders

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.ui.activities.OffreActivity
import com.example.sayaradzmb.ui.activities.AnnonceApercuActivity
import com.example.sayaradzmb.ui.adapter.VehiculeImageAdapter

class OccasionCarsViewHolder (private val view : View): BaseViewHolder<VehiculeOccasion>(view){


    @SuppressLint("SetTextI18n")

    override fun bind(item : VehiculeOccasion){

        view.findViewById<TextView>(R.id.annonce_info).text =
            "${item.version!!.NomMarque} ${item.version!!.NomModele} ${item.version!!.NomVersion}"
        view.findViewById<TextView>(R.id.annonce_price_info).text = item.Prix
        view.findViewById<TextView>(R.id.annonce_annee).text = item.Annee
        view.findViewById<TextView>(R.id.annonce_km).text = item.Km
        view.findViewById<TextView>(R.id.annonce_fuel).text = item.Carburant.toString().replace("\\s".toRegex(), " ")

        val pAdapter = VehiculeImageAdapter(view.context, item.images!!)
        view.findViewById<ViewPager>(R.id.annonce_image).adapter = pAdapter

        view.findViewById<Button>(R.id.annonce_details).setOnClickListener {
            Log.i("Aperçu announce", "am getting in !!")
            // preparé l'activité d'ajout
            val intent = Intent(view.context, AnnonceApercuActivity::class.java)
            //Bundle
            val bundle = Bundle()
            bundle.putParcelable("annonce",item)
            intent.putExtra("annonce",item)
            // lancer l'activité
            ContextCompat.startActivity(view.context, intent, null)
        }


        view.findViewById<Button>(R.id.offer_btn).setOnClickListener {
            Log.i("offer for  announce", "am getting in !!")

            val intent = Intent(view.context, OffreActivity::class.java)
            intent.putExtra("annonce",item)
            // lancer l'activité
            ContextCompat.startActivity(view.context, intent, null)
        }
    }
}