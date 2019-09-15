package com.example.sayaradzmb.ui.adapter.viewholders

import android.app.AlertDialog
import android.support.design.widget.Snackbar
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.ui.adapter.CustomCardsAdapter

class OffersViewHolder(private val view: View , val listener: CustomCardsAdapter.OnClickItemListener) : BaseViewHolder<Offre>(view) , View.OnClickListener {
    override fun onClick(v: View?) {

    }


    override fun bind(item: Offre) {
        val btnAccept = view.findViewById<AppCompatButton>(R.id.btn_accept)
        val btnRefuse = view.findViewById<AppCompatButton>(R.id.btn_declain)

        view.findViewById<TextView>(R.id.person_name).text =
            item.automobiliste.Nom.plus(" " + item.automobiliste.Prenom)
        view.findViewById<TextView>(R.id.date).text = item.Date
        view.findViewById<TextView>(R.id.offer_price).text = item.Montant

        when(item.Etat){
            0 -> {

                btnAccept.visibility = View.VISIBLE
                btnRefuse.visibility = View.VISIBLE

                btnAccept.setOnClickListener {

                    val builder = AlertDialog.Builder(view.context)
                    builder.setTitle("Alert")
                    builder.setIcon(R.drawable.offer_tag)
                    builder.setMessage("You sure you want to accept this offer")
                    //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                    builder.setPositiveButton(android.R.string.yes) { _, which ->
                        Toast.makeText(view.context,
                            android.R.string.yes, Toast.LENGTH_SHORT).show()
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            listener.onClickItem(item.idOffre, "accepter", adapterPosition)
                        }
                    }

                    builder.setNegativeButton(android.R.string.no) { _, which ->
                        Toast.makeText(view.context,
                            android.R.string.no, Toast.LENGTH_SHORT).show()
                    }

                    builder.show()
                }

                btnRefuse.setOnClickListener {
                    Snackbar.make(view, "you have been refused !", Snackbar.LENGTH_LONG).show()
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onClickItem(item.idOffre, "refuser", adapterPosition)
                    }
                }
            }

            1 -> {
               view.findViewById<AppCompatTextView>(R.id.offer_state_accepted).visibility = View.VISIBLE
                view.findViewById<AppCompatTextView>(R.id.offer_state_refused).visibility = View.GONE
                btnAccept.visibility = View.GONE
                btnRefuse.visibility = View.GONE
            }

            3 -> {
                view.findViewById<AppCompatTextView>(R.id.offer_state_refused).visibility = View.VISIBLE
                view.findViewById<AppCompatTextView>(R.id.offer_state_accepted).visibility = View.GONE
                btnAccept.visibility = View.GONE
                btnRefuse.visibility = View.GONE
            }
        }

    }
}