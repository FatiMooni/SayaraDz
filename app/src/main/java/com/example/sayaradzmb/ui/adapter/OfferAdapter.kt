package com.example.sayaradzmb.ui.adapter

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.support.design.chip.Chip
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.model.VehiculeOccasion
import java.util.ArrayList

class OfferAdapter(val context : Context, val offreList : ArrayList<Offre>) : RecyclerView.Adapter<OfferAdapter.ViewHolder>(){


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        //p0.removeAllViews()
        val view = LayoutInflater.from(context).inflate(R.layout.user_offer_card, p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return offreList.size
    }


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = offreList[p1]
        p0.bindInfo(item)
    }


    // class pour binder les information dans les cards view
    inner class ViewHolder (private val objet : View) : RecyclerView.ViewHolder(objet) {
        @TargetApi(23)
         fun bindInfo(item : Offre){
             objet.findViewById<TextView>(R.id.car_info).text = item.vehicule.NomVersion
             objet.findViewById<TextView>(R.id.car_owner).text = item.automobiliste.Nom
             objet.findViewById<TextView>(R.id.car_offer_date).text = item.Date
             objet.findViewById<TextView>(R.id.car_offer_price).text = item.Montant
             objet.findViewById<TextView>(R.id.car_price).text = item.annonce.Prix

             var state = item.Etat
             var chip = objet.findViewById<Chip>(R.id.car_offer_state)

             when(state){
                 0 -> {chip.chipBackgroundColor =
                     context.resources.getColorStateList(R.color.grey_chip,null)
                      chip.text = "Commandée"}

                 1 -> {chip.chipBackgroundColor =
                     context.resources.getColorStateList(R.color.grey_chip,null)
                     chip.text = "Acceptée"}

                 2 -> {chip.chipBackgroundColor =
                 context.resources.getColorStateList(R.color.grey_chip,null)
                 chip.text = "Réfusée"}

                 3 -> {chip.chipBackgroundColor =
                     context.resources.getColorStateList(R.color.grey_chip,null)
                     chip.text = "Annulée"}
             }
         }
    }
}