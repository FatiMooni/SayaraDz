package com.example.sayaradzmb.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sayaradzmb.R

class FollowedCarsCardAdapter(var context : Context, var new_cars : ArrayList<Int>  ) : RecyclerView.Adapter <FollowedCarsCardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        // pour retourner le fichier xml annonce_view sous
        // forme d'une vue pour chaque élément de la liste
        p0.removeAllViews()
        val view = LayoutInflater.from(context).inflate(R.layout.main_card_view, p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return new_cars.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val car_version = new_cars[p1]
        p0.bindInfo(car_version)
    }

    inner class ViewHolder (private val objet : View) : RecyclerView.ViewHolder(objet){

        fun bindInfo(item : Int){
        }
    }
}

