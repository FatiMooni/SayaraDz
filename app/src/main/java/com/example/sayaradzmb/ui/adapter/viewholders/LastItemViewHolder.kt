package com.example.sayaradzmb.ui.adapter.viewholders

import android.support.design.widget.FloatingActionButton
import android.view.View
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.ui.adapter.AcceuilleCardAdapter

class LastItemViewHolder(val view: View, val listener: AcceuilleCardAdapter.OnCardButtonListener) :
    BaseViewHolder<Comparable<*>>(view) {


    override fun bind(item: Comparable<*>) {

        val btn = view.findViewById<FloatingActionButton>(R.id.btn_more)

        btn.setOnClickListener {
            when (item) {
                is VehiculeOccasion ->
                    listener.OnCardButton(null, AcceuilleCardAdapter.TYPE_ITEM_OCCASION)

                is Int ->
                    listener.OnCardButton(null, AcceuilleCardAdapter.TYPE_ITEM_NEW)

                else -> throw IllegalArgumentException("Invalid type of data $item")
            }
        }
    }

}