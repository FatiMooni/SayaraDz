package com.example.sayaradzmb.ui.adapter.viewholders

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.design.card.MaterialCardView
import android.support.design.chip.Chip
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.model.UserOffre
import com.example.sayaradzmb.ui.adapter.CustomCardsAdapter

class UserOffersViewHolder(val view : View,
                           private val listener: CustomCardsAdapter.OnClickItemListener)
    : BaseViewHolder<UserOffre>(view) , View.OnClickListener , View.OnLongClickListener{
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
    }

    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     *
     * @return true if the callback consumed the long click, false otherwise.
     */
    override fun onLongClick(v: View?): Boolean {
        return true
    }

    init {
        val card = view.findViewById<MaterialCardView>(R.id.root_card)
        card.setOnClickListener(this)
        card.setOnLongClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun bind(item: UserOffre) {
        view.findViewById<TextView>(R.id.car_info).text = item.vehicule.NomVersion
        view.findViewById<TextView>(R.id.car_owner).text = item.automobiliste.Nom
        view.findViewById<TextView>(R.id.car_offer_date).text = item.Date
        view.findViewById<TextView>(R.id.car_offer_price).text = item.Montant
        view.findViewById<TextView>(R.id.car_price).text = item.annonce.Prix

        val state = item.Etat
        val chip = view.findViewById<Chip>(R.id.car_offer_state)


        view.findViewById<ImageButton>(R.id.card_menu).setOnClickListener {
           listener.onPopupMenuRequested(item, view , adapterPosition)
        }

        when (state) {
            0 -> {
                chip.chipBackgroundColor =
                    view.context.resources.getColorStateList(R.color.grey_chip, null)
                chip.text = "Commandée"
            }

            1 -> {
                chip.chipBackgroundColor =
                    view.context.resources.getColorStateList(R.color.green_chip, null)
                chip.text = "Acceptée"
            }

            3 -> {
                chip.chipBackgroundColor =
                    view.context.resources.getColorStateList(R.color.red_chip, null)
                chip.text = "Réfusée"
            }

            2 -> {
                chip.chipBackgroundColor =
                    view.context.resources.getColorStateList(R.color.grey_chip, null)
                chip.text = "Annulée"
            }
        }
    }

}