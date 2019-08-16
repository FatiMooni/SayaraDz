package com.example.sayaradzmb.ui.adapter.viewholders

import android.app.AlertDialog
import android.support.design.widget.Snackbar
import android.support.v7.widget.AlertDialogLayout
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Annonce
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.model.Version
import com.example.sayaradzmb.ui.adapter.OccasionCarsAdapter

class OffersViewHolder(private val view: View , val listener: OccasionCarsAdapter.OnClickItemListener) : BaseViewHolder<Offre>(view) , View.OnClickListener {
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
                    Snackbar.make(view, "you have been accepeted", Snackbar.LENGTH_LONG).show()
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onClickItem(item.idOffre, "accepter", adapterPosition)
                    }

                    val builder = AlertDialog.Builder(view.context)
                    builder.setTitle("Androidly Alert")
                    builder.setMessage("We have a message")
                    //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                    builder.setPositiveButton(android.R.string.yes) { _, which ->
                        Toast.makeText(view.context,
                            android.R.string.yes, Toast.LENGTH_SHORT).show()
                    }

                    builder.setNegativeButton(android.R.string.no) { _, which ->
                        Toast.makeText(view.context,
                            android.R.string.no, Toast.LENGTH_SHORT).show()
                    }

                    builder.setNeutralButton("Maybe") { _, which ->
                        Toast.makeText(view.context,
                            "Maybe", Toast.LENGTH_SHORT).show()
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