package com.example.sayaradzmb.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.ui.activities.OffreActivity
import com.example.sayaradzmb.ui.activities.OffreApercuActivity

class CarsViewHolder (val objet : View): RecyclerView.ViewHolder(objet){


    @SuppressLint("SetTextI18n")
    fun bindInfo(item : VehiculeOccasion){
        objet.findViewById<TextView>(R.id.annonce_info).text =
            "${item.version!!.NomMarque} ${item.version!!.NomModele} ${item.version!!.NomVersion}"
        objet.findViewById<TextView>(R.id.annonce_price_info).text = item.Prix
        objet.findViewById<TextView>(R.id.annonce_annee).text = item.Annee
        objet.findViewById<TextView>(R.id.annonce_km).text = item.Km
        objet.findViewById<TextView>(R.id.annonce_fuel).text = item.Carburant
        val pAdapter = VehiculeImageAdapter(objet.context, item.images!!)
        objet.findViewById<ViewPager>(R.id.annonce_image).adapter = pAdapter

        objet.findViewById<Button>(R.id.annonce_details).setOnClickListener {
            // preparé l'activité d'ajout
            val intent = Intent(objet.context, OffreApercuActivity::class.java)
            //Bundle
            val bundle = Bundle()
            bundle.putParcelable("annonce",item)
            intent.putExtra("annonce",item)
            // lancer l'activité
            ContextCompat.startActivity(objet.context, intent, null)

        }


        objet.findViewById<Button>(R.id.offer_btn).setOnClickListener {
            val intent = Intent(objet.context, OffreActivity::class.java)
            intent.putExtra("annonce",item)
            // lancer l'activité
            ContextCompat.startActivity(objet.context, intent, null)
        }
    }
}