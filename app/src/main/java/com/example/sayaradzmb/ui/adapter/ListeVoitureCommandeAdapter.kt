package com.example.sayaradzmb.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.ColorHelper
import com.example.sayaradzmb.helper.FragmentHelper
import com.example.sayaradzmb.model.Couleur
import com.example.sayaradzmb.model.VoitureCommande
import com.example.sayaradzmb.ui.activities.fragments.NouveauCommandeFragment
import org.w3c.dom.Text

class ListeVoitureCommandeAdapter(
    private val voitureList: ArrayList<VoitureCommande>,
    internal var context: Context
) : RecyclerView.Adapter<ListeVoitureCommandeAdapter.ListeVoitureCommandeViewHolder>() {


    private var view : View?=null
    private var buttonColor : Button?=null
    private var root_layout :ViewGroup? = null
    private var commander = (context as Activity).findViewById(R.id.neuf_tech_card_commande_button) as Button
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListeVoitureCommandeViewHolder {
        //inflate the layout file
        val voitureView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_voiture, parent, false)
        view=voitureView
        root_layout = (context as Activity).findViewById(R.id.root_layout) as RelativeLayout
        return ListeVoitureCommandeViewHolder(voitureView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ListeVoitureCommandeViewHolder, position: Int) {
        var voiture = voitureList.get(position)
        /**
         * buttonCouleur
         */
        var drawable = holder.color.background as GradientDrawable
        ColorHelper.colorerButton(drawable,voiture.Couleur!!.CodeHexa!!)
        /**
         * quantite
         */
        holder.quantite.text = "${(voiture.quantite)} voiture"

        /**
         * price Camcule
         */
        var voitureOption = ArrayList<Int>()
        // price options
        var priceOptions = 0
        //price Color
        var priceCouleur = voiture.Couleur!!.tarifCouleur!!.Prix
        // price base
        var priceBase = voiture.tarifBase!!.Prix

        voiture.options.forEach {
            voitureOption.add(it.CodeOption!!)
            priceOptions+= it.tarifOption!!.Prix
        }
        var priceTotal = priceBase+priceCouleur+priceOptions

        holder.price.text = "${priceTotal} DA"
        /**
         * options
         */
        holder.options.setOnClickListener{
            var builder : AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Liste des Option Correspandante :")
            var listOption = Array<CharSequence?>(voiture.options.size,init = {
                index -> ""
            })
            var i = 0
            voiture.options.forEach {
                option -> listOption.set(i++,option.NomOption!!)
            }
            builder.setItems(listOption,DialogInterface.OnClickListener { dialog, which ->

            })
            var dialog :AlertDialog = builder.create()
            dialog.show()
        }
        /**
         * View
         */
        holder.viewCard.setOnClickListener {
            commander.visibility = View.VISIBLE
            if (position+1 == 1 ) commander.text = "Commander ${position+1} ere voiture"
            else commander.text = "Commander ${position+1}eme voiture"
        }
        /**
         * Commande
         */
        commander.setOnClickListener {
            FragmentHelper.changeFragment(voiture,context as FragmentActivity,NouveauCommandeFragment(),"voitureCommande","toCommande",R.id.fragment_id)
        }
    }

    override fun getItemCount(): Int {
        return voitureList.size
    }

    inner class ListeVoitureCommandeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var viewCard : CardView = view.findViewById(R.id.card_item_cars)
        internal var quantite : TextView = view.findViewById(R.id.text_voiture_quantite)
        internal var options : Button = view.findViewById(R.id.button_voiture_options)
        private var colorLayout  = view.findViewById(R.id.layout_color) as View
        internal var color : Button = colorLayout.findViewById(R.id.item_couleur_button)
        internal var price : TextView = view.findViewById(R.id.text_voiture_prix)
    }
}