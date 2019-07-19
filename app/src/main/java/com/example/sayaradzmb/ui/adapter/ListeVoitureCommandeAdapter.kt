package com.example.sayaradzmb.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Couleur
import com.example.sayaradzmb.model.VoitureCommande

class ListeVoitureCommandeAdapter(
    private val voitureList: ArrayList<VoitureCommande>,
    internal var context: Context
) : RecyclerView.Adapter<ListeVoitureCommandeAdapter.ListeVoitureCommandeViewHolder>() {


    private var view : View?=null
    private var buttonColor : Button?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListeVoitureCommandeViewHolder {
        //inflate the layout file
        val voitureView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_voiture, parent, false)
        view=voitureView
        return ListeVoitureCommandeViewHolder(voitureView)
    }

    override fun onBindViewHolder(holder: ListeVoitureCommandeViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return voitureList.size
    }

    inner class ListeVoitureCommandeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}