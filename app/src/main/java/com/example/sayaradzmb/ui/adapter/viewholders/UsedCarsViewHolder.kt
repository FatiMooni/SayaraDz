package com.example.sayaradzmb.ui.adapter.viewholders

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.View
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.VehiculeOccasion
import com.squareup.picasso.Picasso

class UsedCarsViewHolder(val view: View) : BaseViewHolder<VehiculeOccasion>(view) {
    override fun bind(item: VehiculeOccasion) {

        val img = view.findViewById<AppCompatImageView>(R.id.car_main_image_holder)

        view.findViewById<AppCompatTextView>(R.id.car_title).text =
            item.version!!.NomMarque.plus(" " + item.version!!.NomModele).plus(" " + item.version!!.NomVersion)
        view.findViewById<AppCompatTextView>(R.id.car_descrip).text = item.Prix.plus("DZA")

        if(item.images!!.isNotEmpty()){
            Picasso.get().load(item.images!![0].CheminImage).centerInside().fit().into(img)
        }

    }
}