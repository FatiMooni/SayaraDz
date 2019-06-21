package com.example.sayaradzmb.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.VehiculeOccasion
import java.util.ArrayList

class OccasionCarsAdapter(val context : Context, val annonceList : ArrayList<VehiculeOccasion>) : RecyclerView.Adapter<OccasionCarsAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        //p0.removeAllViews()
        val view = LayoutInflater.from(context).inflate(R.layout.used_cars_view, p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return annonceList.size
    }


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val annonceItem = annonceList[p1]
        p0.bindInfo(annonceItem)
    }


    // class pour binder les information dans les cards view
    inner class ViewHolder (private val objet : View) : RecyclerView.ViewHolder(objet){


        @SuppressLint("SetTextI18n")
        fun bindInfo(item : VehiculeOccasion){
            objet.findViewById<TextView>(R.id.annonce_info).text =
                "${item.version.NomMarque} ${item.version.NomModele} ${item.version.NomVersion}"
            objet.findViewById<TextView>(R.id.annonce_price_info).text = item.Prix
            objet.findViewById<TextView>(R.id.annonce_annee).text = item.Annee
            objet.findViewById<TextView>(R.id.annonce_km).text = item.Km
            objet.findViewById<TextView>(R.id.annonce_fuel).text = item.Carburant
            val pAdapter = VehiculeImageAdapter(context, item.images!!)
            objet.findViewById<ViewPager>(R.id.annonce_image).adapter = pAdapter

        }
    }
}