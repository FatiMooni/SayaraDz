package com.example.sayaradzmb.ui.adapter.viewholders

import android.util.Log
import android.view.View
import com.example.sayaradzmb.model.Annonce
import com.example.sayaradzmb.ui.adapter.CustomCardsAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.annonce_view.view.*

class AnnonceViewHolder(val objet: View, val listener: CustomCardsAdapter.OnClickItemListener) :
    BaseViewHolder<Annonce>(objet) {


    override fun bind(item: Annonce) {
        val ver = item.version!!
        objet.annonce_info.text = ver.NomMarque.plus(" ").plus(ver.NomModele).plus(" ").plus(ver.NomVersion)
        objet.annonce_price_info.text = item.Prix
        objet.annonce_offer_info.text = item.NombreOffres.toString()

        if (item.images!!.isNotEmpty()) {
            Picasso.get().load(item.images!![0].CheminImage).into(objet.annonce_image)
        }

        //pour popup widget
        objet.delete_icon_btn.setOnClickListener {
            listener.onPopupMenuRequested(item, objet, adapterPosition)
        }


        objet.offer_num.setOnClickListener {
            listener.onButtonClickItem(item,adapterPosition)
        }


    }
}