package com.example.sayaradzmb.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alespero.expandablecardview.ExpandableCardView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.activities.fragments.NouveauRechercheCars
import com.example.sayaradzmb.model.version
import com.squareup.picasso.Picasso

class VersionAdapter(
    private val versionList: ArrayList<version>,
    internal var context: Context,
    internal var view : View
) : RecyclerView.Adapter<VersionAdapter.VersionViewHolder>() {
    private var onSearchPressed : NouveauRechercheCars.OnSearchPressed? = null
    private var currentCodeVersion: Int = -1
    var versionDropDown = view.findViewById<ExpandableCardView>(R.id.fnt_ecv_version)
    var search = view.findViewById<Button>(R.id.search_button)
    var frag = 0
    /**
     * Second constrator
     */

    constructor(versionList: ArrayList<version>, context: Context, view : View,  onSearchPressed : NouveauRechercheCars.OnSearchPressed?) : this(versionList,context,view) {
        this.onSearchPressed = onSearchPressed
        frag = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VersionViewHolder {
        //inflate the layout file
        val versionView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_version, parent, false)


        return VersionViewHolder(versionView)
    }

    override fun onBindViewHolder(holder: VersionViewHolder, position: Int) {
        val version = versionList.get(position)
        var imageSuivi = holder.suivieImage
        holder.nomVersion.text = version.NomVersion
        holder.nomVersion.setOnClickListener {
            currentCodeVersion = version.CodeVersion!!
            versionDropDown.setTitle(version.NomVersion)
            versionDropDown.collapse()

            //settings for each fragment
            when(frag) {
                1 -> {  search.visibility=View.VISIBLE
                        Log.i("modele : ",version.toString())
                        search.setOnClickListener{
                            onSearchPressed!!.envoyerFragment(1,version)}
                }

                0-> {

                }
            }
        }
        holder.suivieImage.setOnClickListener {
            if (holder.suivieImage.tag == "nonSuivi"){
                /**
                 * faire l'abonnement
                 */
                Picasso.get().load(R.drawable.star).into(imageSuivi)
                imageSuivi.tag = "Suivi"
            }else{
                /**
                 * desabonner
                 */
                Picasso.get().load(R.drawable.star_vide).into(imageSuivi)
                imageSuivi.tag = "nonSuivi"
            }
        }
    }

    override fun getItemCount(): Int {
        return versionList.size
    }

    inner class VersionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var item = view.findViewById<LinearLayout>(R.id.item_version)
        internal var suivieImage = view.findViewById<ImageView>(R.id.im_image_suiviversion)
        internal var nomVersion = view.findViewById<TextView>(R.id.im_text_nomversion)
    }
}
