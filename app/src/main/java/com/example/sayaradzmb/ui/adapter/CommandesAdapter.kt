package com.example.sayaradzmb.ui.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Commande
import java.util.*

class CommandesAdapter(val context: Context, private val commandList: ArrayList<Commande>) :
    RecyclerView.Adapter<CommandesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        //p0.removeAllViews()
        val view = LayoutInflater.from(context).inflate(R.layout.user_commande_card, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commandList.size
    }


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = commandList[p1]
        p0.bindInfo(item)
    }


    // class pour binder les information dans les cards view
    inner class ViewHolder(private val objet: View) : RecyclerView.ViewHolder(objet) {


        @SuppressLint("SetTextI18n")
        fun bindInfo(item: Commande) {
            /*objet.findViewById<AppCompatTextView>(R.id.car_info).text = item.vehicule.NomMarque.plus(" ")
                           .plus(item.vehicule.NomModele).plus(" ")
                           .plus(item.vehicule.NomVersion).plus(" ")
            objet.findViewById<AppCompatTextView>(R.id.car_num).text = item.vehicule.Concessionaire.plus(" / ").plus(item.vehicule.NumChassis.toString())

            if (item.Reservation != null)  objet.findViewById<AppCompatTextView>(R.id.car_reserv).text = item.Reservation.toString().plus(" DZA")
*/
        }
    }
}