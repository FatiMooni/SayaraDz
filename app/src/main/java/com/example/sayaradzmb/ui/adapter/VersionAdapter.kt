package com.example.sayaradzmb.ui.adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alespero.expandablecardview.ExpandableCardView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.ui.activities.fragments.NouveauRechercheCars
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.helper.SuiviVoitureHelper
import com.example.sayaradzmb.model.Version

import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.servics.VersionService
import com.example.sayaradzmb.ui.activities.fragments.NouveauAfficheTechnique
import com.pusher.pushnotifications.PushNotifications
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VersionAdapter(
    private val versionList: ArrayList<Version>,
    internal var context: Context,
    internal var view : View,
    private var versionListFiltree : ArrayList<Version>
) : RecyclerView.Adapter<VersionAdapter.VersionViewHolder>(),Filterable, SharedPreferenceInterface, SuiviVoitureHelper {



    private var onSelectedItem : ((Version) -> Unit)? = null

    private var currentCodeVersion: Int = -1
    var versionDropDown = view.findViewById<ExpandableCardView>(R.id.fnt_ecv_version)
    var search = view.findViewById<Button>(R.id.search_button)
    var frag = 1
    /**
     * Second constrator
     */



    constructor(versionList: ArrayList<Version>, context: Context, view : View, versionListFiltree: ArrayList<Version>,listener: (Version) -> Unit) : this(versionList,context,view,versionListFiltree) {
        this.onSelectedItem = listener
        frag = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VersionViewHolder {
        //inflate the layout file
        val versionView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_version, parent, false)


        return VersionViewHolder(versionView)
    }

    override fun onBindViewHolder(holder: VersionViewHolder, position: Int) {
        val version = versionListFiltree.get(position)
        var imageSuivi = holder.suivieImage
        holder.nomVersion.text = version.NomVersion

        toggleSuivi(version.suivie,imageSuivi,R.drawable.star,R.drawable.star_vide)


            //settings for each fragment
            when(frag) {
                1 -> {
                    holder.nomVersion.setOnClickListener {
                        currentCodeVersion = version.CodeVersion!!
                        versionDropDown.setTitle(version.NomVersion)
                        versionDropDown.collapse()
                        search.visibility = View.VISIBLE
                        Log.i("modele : ", version.toString())
                        search.setOnClickListener {
                            val bundel = Bundle()
                            bundel.putParcelable("affiche", version)
                            val afficheFragment = NouveauAfficheTechnique()
                            afficheFragment.arguments = bundel
                            val activity = context as FragmentActivity
                            activity.supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_id, afficheFragment, "toAfficheFrag")
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }
                0-> {
                        holder.bind(version, onSelectedItem!!)
                }
            }

        holder.suivieImage.setOnClickListener {
            if (holder.suivieImage.tag == "nonSuivi"){
                /**
                 * faire l'abonnement
                 */
                val vService =  ServiceBuilder.buildService(VersionService::class.java)
                val requeteAppel = vService.suivreVersion(version.CodeVersion!!,avoirInfoUser(this.context))
                requeteAppel.enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>): Unit =
                        if(response.isSuccessful){
                            println(response.body().toString())
                            PushNotifications.addDeviceInterest("VERSION_${version.CodeVersion}")
                            processusSuivre(R.drawable.star,imageSuivi,"Suivi")
                        }else{
                            println("la liste modele non reconnue ${response}")

                        }
                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        Log.w("failConnexion","la liste modele non reconnue ${t.message}")
                    }
                })

            }else{
                /**
                 * desabonner
                 */
                val vService =  ServiceBuilder.buildService(VersionService::class.java)
                val requeteAppel = vService.desuivreVersion(version.CodeVersion!!,avoirIdUser(this.context)!!)
                requeteAppel.enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>): Unit =
                        if(response.isSuccessful){
                            println(response.body().toString())
                            PushNotifications.removeDeviceInterest("VERSION_${version.CodeVersion}")
                            processusSuivre(R.drawable.star_vide,imageSuivi,"nonSuivi")
                        }else{
                            println("la liste modele non reconnue ${response}")
                        }
                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        Log.w("failConnexion","la liste modele non reconnue ${t.message}")
                    }
                })
            }
        }
    }

    override fun getItemCount(): Int {
        return versionListFiltree.size
    }

    //val listener (view : View) -> ( AdapterView.OnItemClickListener?

    inner class VersionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var item = view.findViewById<LinearLayout>(R.id.item_version)
        internal var suivieImage = view.findViewById<ImageView>(R.id.im_image_suiviversion)
        internal var nomVersion = view.findViewById<TextView>(R.id.im_text_nomversion)

        fun bind(obj: Version, listener: (Version) -> Unit) = with(view) {
            nomVersion.text = obj.NomVersion
            item.setOnClickListener {
                listener(obj)
                versionDropDown.setTitle(obj.NomVersion)
                versionDropDown.collapse()
            }
        }
    }

    public override fun getFilter(): Filter {
        return object : Filter() {
            protected override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    versionListFiltree= versionList
                    Log.i("marquefiltree",versionListFiltree.toString())
                } else {
                    val filteredList = ArrayList<Version>()
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
                versionListFiltree = filterResults.values as ArrayList<Version>
                notifyDataSetChanged()
            }
        }
    }


}
