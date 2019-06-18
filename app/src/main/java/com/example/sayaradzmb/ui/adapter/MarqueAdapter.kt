package com.example.sayaradzmb.ui.adapter

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alespero.expandablecardview.ExpandableCardView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.Repository.controller.ModeleController
import com.example.sayaradzmb.ui.activities.Fragments.NouveauRechercheCars
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.helper.SearchViewInterface
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.model.Modele
import com.squareup.picasso.Picasso


class MarqueAdapter(
    private val marqueList: ArrayList<Marque>,
    internal var context: Context,
    internal var view : View,
    private var onSearchPressed : NouveauRechercheCars.OnSearchPressed?,
    private var marqueListFiltree : ArrayList<Marque>,
    private val activity : FragmentActivity
) : RecyclerView.Adapter<MarqueAdapter.MarqueViewHolder>(), RecycleViewHelper,Filterable,SearchViewInterface,SharedPreferenceInterface {

    /**
     * Declaration
     */
    var marqueview : View? = null
    var marqueDropDown = view.findViewById<ExpandableCardView>(R.id.fnt_ecv_marque)
    var modeleDropDown = view.findViewById<ExpandableCardView>(R.id.fnt_ecv_modele)
    var versionDropDown = view.findViewById<ExpandableCardView>(R.id.fnt_ecv_version)
    private var modeleAdapter : ModeleAdapter? = null
    private var modeleList = ArrayList<Modele>()
    override var itemRecycleView : RecyclerView? = null
    private var currentCodeMarque : Int = -1
    var search = view.findViewById<Button>(R.id.search_button)

    /**
     * OnCreate
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarqueViewHolder {
        //inflate the layout file
        val marqueView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_marque, parent, false)
        marqueview = marqueView;
        return MarqueViewHolder(marqueView)
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: MarqueViewHolder, position: Int) {
        val marque = marqueListFiltree.get(position)
        holder.nomMarque.text = marque.NomMarque

        if(marque.images!!.isNotEmpty()){
            Picasso.get().load(marque.images[0].CheminImage).into(holder.logoImage)
        }
        holder.item.setOnClickListener(View.OnClickListener {
            currentCodeMarque = marque.CodeMarque!!
            search.visibility=View.GONE
            marqueDropDown.setTitle(marque.NomMarque)
            marqueDropDown.collapse()
            modeleDropDown.visibility=View.VISIBLE
            modeleDropDown.collapse()
            modeleDropDown.setTitle("Modele")
            versionDropDown.visibility=View.GONE
            init(view)
            requeteModele()
            if(marqueview == null) Log.i("marqueview","marqueview null")
            initSearchView(activity,view!!,modeleAdapter!!,R.id.search_bar_modele)
        })
    }

    /**
     *
     */
    override fun getItemCount(): Int {
        return marqueListFiltree.size
    }

    /**
     *
     */
    fun addAllwithclear(marqueLists : ArrayList<Marque>){
        //marqueList.clear()
        marqueList.addAll(marqueLists)
        notifyDataSetChanged()
    }

    /**
     *
     */
    inner class MarqueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            internal var item = view.findViewById<LinearLayout>(R.id.item_marque)
            internal var logoImage = view.findViewById<ImageView>(R.id.im_image_logomarque)
            internal var nomMarque = view.findViewById<TextView>(R.id.im_text_nommarque)
    }

    /**
     * les fonction d'essai
     */
    private fun init(v : View){
        modeleAdapter = ModeleAdapter(modeleList,v.context,view,onSearchPressed,modeleList,activity!!)
        initLineaire(v,R.id.imd_rv_modele, LinearLayoutManager.VERTICAL,modeleAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }

    private fun requeteModele(){
        modeleList.clear()
        modeleList.addAll(ModeleController.getAllModele(this.context,currentCodeMarque))
    }

    /**
     *
     */
    public override fun getFilter(): Filter {
        return object : Filter() {
            protected override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    marqueListFiltree = marqueList
                    Log.i("marquefiltree",marqueListFiltree.toString())
                } else {
                    val filteredList = ArrayList<Marque>()
                    for (row in marqueList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.NomMarque?.toLowerCase()?.contains(charString.toLowerCase())!!) {
                            filteredList.add(row)
                        }
                    }
                    marqueListFiltree = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = marqueListFiltree
                return filterResults
            }

            protected override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                marqueListFiltree = filterResults.values as ArrayList<Marque>
                notifyDataSetChanged()
            }
        }
    }

}
