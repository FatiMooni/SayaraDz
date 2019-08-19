package com.example.sayaradzmb.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import com.example.sayaradzmb.ui.adapter.viewholders.UserOffersViewHolder

class ItemLookup (val rv : RecyclerView): ItemDetailsLookup<Long>() {
    /**
     * For the RecyclerView Selection addon to work correctly,
     * whenever the user touches the RecyclerView widget,
     * it is needed to translate the coordinates of the touch into an ItemDetails object.
     */
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = rv.findChildViewUnder(event.x, event.y)
        if(view != null) {
            return (rv.getChildViewHolder(view) as UserOffersViewHolder).getItemDetails()

        }
        return null
    }
}