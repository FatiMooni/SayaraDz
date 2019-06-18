package com.example.sayaradzmb.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alespero.expandablecardview.ExpandableCardView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.Repository.controller.VersionController
import com.example.sayaradzmb.ui.activities.Fragments.NouveauRechercheCars
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.helper.SuiviVoitureHelper
import com.example.sayaradzmb.model.version

class VersionAdapter(
    private val versionList: ArrayList<version>,
    internal var context: Context,
    internal var view : View,
    private var onSearchPressed : NouveauRechercheCars.OnSearchPressed?,
    private var versionListFiltree : ArrayList<version>
) : RecyclerView.Adapter<VersionAdapter.VersionViewHolder>(),Filterable,SharedPreferenceInterface,SuiviVoitureHelper {

    /**
     * Declaration
     */
    private var currentCodeVersion: Int = -1
    var versionDropDown = view.findViewById<ExpandableCardView>(R.id.fnt_ecv_version)
    var search = view.findViewById<Button>(R.id.search_button)

    /**
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VersionViewHolder {
        //inflate the layout file
        val versionView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_version, parent, false)


        return VersionViewHolder(versionView)
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: VersionViewHolder, position: Int) {
        val version = versionListFiltree.get(position)
        var imageSuivi = holder.suivieImage
        holder.nomVersion.text = version.NomVersion
        toggleSuivi(version.suivie,imageSuivi,R.drawable.star,R.drawable.star_vide)
        holder.nomVersion.setOnClickListener {
            currentCodeVersion = version.CodeVersion!!
            versionDropDown.setTitle(version.NomVersion)
            versionDropDown.collapse()
            search.visibility=View.VISIBLE
            Log.i("modele : ",version.toString())
            search.setOnClickListener{
                onSearchPressed!!.envoyerFragment(1,version)
            }
        }
        holder.suivieImage.setOnClickListener {
            if (holder.suivieImage.tag == "nonSuivi"){
                /**
                 * faire l'abonnement
                 */
                VersionController.suivreVersion(version,this.context)
                processusSuivre(R.drawable.star,imageSuivi,"Suivi")
            }else{
                /**
                 * desabonner
                 */
               VersionController.desuivreVersion(version,this.context)
                processusSuivre(R.drawable.star_vide,imageSuivi,"nonSuivi")
            }
        }
    }

    override fun getItemCount(): Int {
        return versionListFiltree.size
    }

    inner class VersionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var item = view.findViewById<LinearLayout>(R.id.item_version)
        internal var suivieImage = view.findViewById<ImageView>(R.id.im_image_suiviversion)
        internal var nomVersion = view.findViewById<TextView>(R.id.im_text_nomversion)
    }

    public override fun getFilter(): Filter {
        return object : Filter() {
            protected override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    versionListFiltree= versionList
                    Log.i("marquefiltree",versionListFiltree.toString())
                } else {
                    val filteredList = ArrayList<version>()
                    for (row in versionList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.NomVersion?.toLowerCase()?.contains(charString.toLowerCase())!!) {
                            filteredList.add(row)
                        }
                    }
                    versionListFiltree = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = versionListFiltree
                return filterResults
            }

            protected override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                versionListFiltree = filterResults.values as ArrayList<version>
                notifyDataSetChanged()
            }
        }
    }


}
