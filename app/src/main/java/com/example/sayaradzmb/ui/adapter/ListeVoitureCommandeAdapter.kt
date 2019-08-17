package com.example.sayaradzmb.ui.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.v4.app.FragmentActivity
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

class ListeVoitureCommandeAdapter(
    private val voitureList: ArrayList<VoitureCommande>,
    internal var context: Context
) : RecyclerView.Adapter<ListeVoitureCommandeAdapter.ListeVoitureCommandeViewHolder>() {


    private var view : View?=null
    private var buttonColor : Button?=null
    private var root_layout :ViewGroup? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListeVoitureCommandeViewHolder {
        //inflate the layout file
        val voitureView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_voiture, parent, false)
        view=voitureView
        root_layout = (context as Activity).findViewById(R.id.root_layout) as RelativeLayout
        return ListeVoitureCommandeViewHolder(voitureView)
    }

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
        holder.quantite.text = (voiture.quantite).toString()

        /**
         * options
         */
        holder.options.setOnClickListener{
            // Initialize a new layout inflater instance
            val inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            // Inflate a custom view using layout inflater
            val view = inflater.inflate(R.layout.item_option_list,null)

            // Initialize a new instance of popup window
            val popupWindow = PopupWindow(
                view, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
            )
            popupWindow.isFocusable = true

            // Set an elevation for the popup window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.elevation = 10.0F
            }


            // If API level 23 or higher then execute the code
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.TOP
                popupWindow.enterTransition = slideIn

                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.RIGHT
                popupWindow.exitTransition = slideOut

            }



            // Set click listener for popup window's text view
            var textOption = view.findViewById<TextView>(R.id.item_option_text)
            textOption.text = voiture.options.get(0).NomOption
            // Set a click listener for popup's button widget


            // Set a dismiss listener for popup window
            popupWindow.setOnDismissListener {
                Toast.makeText(context,"Popup closed",Toast.LENGTH_SHORT).show()
            }


            // Finally, show the popup window on app
            TransitionManager.beginDelayedTransition(root_layout)
            popupWindow.showAtLocation(
                root_layout, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )
        }

        /**
         * Commande
         */
        holder.commander.setOnClickListener {
            FragmentHelper.changeFragment(voiture,context as FragmentActivity,NouveauCommandeFragment(),"voitureCommande","toCommande",R.id.fragment_id)
        }
    }

    override fun getItemCount(): Int {
        return voitureList.size
    }

    inner class ListeVoitureCommandeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var quantite : TextView = view.findViewById(R.id.text_voiture_quantite)
        internal var options : Button = view.findViewById(R.id.button_voiture_options)
        private var colorLayout  = view.findViewById(R.id.layout_color) as View
        internal var color : Button = colorLayout.findViewById(R.id.item_couleur_button)
        internal var commander : Button = view.findViewById(R.id.neuf_tech_card_commande_button)
    }
}