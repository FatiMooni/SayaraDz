package com.example.sayaradzmb.ui.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Version
import com.squareup.picasso.Picasso

class ListeVoitureFavorisAdapter(
    private val voitureList: ArrayList<Version>,
    internal var context: Context
) : RecyclerView.Adapter<ListeVoitureFavorisAdapter.ListeVoitureFavorisViewHolder>() {


    private var view : View?=null
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListeVoitureFavorisViewHolder {
        //inflate the layout file
        val voitureView =
            LayoutInflater.from(parent.context).inflate(R.layout.favoris_view, parent, false)
        view=voitureView
        return ListeVoitureFavorisViewHolder(voitureView)
    }

    override fun onBindViewHolder(holder: ListeVoitureFavorisViewHolder, position: Int) {
        val version = voitureList[position]

        holder.nom.text = version.NomVersion
        holder.prix.text = version.lignetarif?.Prix?.toString()
        Picasso.get().load(version.images?.get(0)?.CheminImage).resize(50,50).into(holder.imageVersion)
    }

    override fun getItemCount(): Int {
        return voitureList.size
    }

    inner class ListeVoitureFavorisViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var viewCard : CardView = view.findViewById(R.id.favoris_card)
        internal var nom : TextView = view.findViewById(R.id.favoris_name_car)
        internal var prix : TextView = view.findViewById(R.id.favoris_price)
        internal var imageVersion : ImageView = view.findViewById(R.id.favoris_image_car)
    }
}
