package com.example.sayaradzmb.ui.adapter

import android.annotation.TargetApi
import android.content.Context
import android.support.design.chip.Chip
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Offre
import kotlinx.android.synthetic.main.user_offer_card.view.*
import java.util.*

class OfferAdapter(val context: Context, val offreList: ArrayList<Offre>) :
    RecyclerView.Adapter<OfferAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        //p0.removeAllViews()
        val view = LayoutInflater.from(context).inflate(R.layout.user_offer_card, p0, false)
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
    inner class ViewHolder(private val objet: View) : RecyclerView.ViewHolder(objet) {

        @TargetApi(23)
        fun bindInfo(item: Offre) {

        }


    }
}