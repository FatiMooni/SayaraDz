package com.example.sayaradzmb.ui.adapter.viewholders

import android.support.v7.widget.AppCompatTextView
import android.view.View
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Commande
import de.hdodenhof.circleimageview.CircleImageView

class CommandeViewHolder(val view: View) : BaseViewHolder<Commande>(view) {
    override fun bind(item: Commande) {
        val stepOne = view.findViewById<CircleImageView>(R.id.first_step)
        val stepTwo = view.findViewById<CircleImageView>(R.id.second_step)
        val stepThree = view.findViewById<CircleImageView>(R.id.third_step)
        val stepFour = view.findViewById<CircleImageView>(R.id.fourth_step)
        val stepThreeTitle = view.findViewById<AppCompatTextView>(R.id.third_step_title)

        view.findViewById<AppCompatTextView>(R.id.car_info).text = item.vehicule.NomVersion
        view.findViewById<AppCompatTextView>(R.id.car_num).text = item.Date

        when (item.Etat) {
            0 , 1 , 2 -> {
                stepOne.background =
                    view.resources.getDrawable(R.drawable.accepted, null)
                if (item.Reservation != 0) {
                    view.findViewById<AppCompatTextView>(R.id.car_reserv).text = item.Reservation.toString()
                    stepTwo.background =
                        view.resources.getDrawable(R.drawable.accepted, null)
                }
                if(item.Etat == 1){
                    stepThree.background =
                        view.resources.getDrawable(R.drawable.accepted, null)
                    stepThreeTitle.text = "Acceptée"
                }

                if(item.Etat == 2){
                    stepThree.background =
                        view.resources.getDrawable(R.drawable.canceld, null)
                    stepFour.background =
                        view.resources.getDrawable(R.drawable.canceld, null)
                    stepThreeTitle.text = "Réfusée"

                }
            }


            3 -> {
                stepOne.background =
                    view.resources.getDrawable(R.drawable.canceld, null)
                stepTwo.background =
                    view.resources.getDrawable(R.drawable.canceld, null)
                stepThree.background =
                    view.resources.getDrawable(R.drawable.canceld, null)
                stepFour.background =
                    view.resources.getDrawable(R.drawable.canceld, null)
            }
        }
    }
}