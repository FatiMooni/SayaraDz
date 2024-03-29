package com.example.sayaradzmb.ui.adapter

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.graphics.ColorSpace
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
import com.example.sayaradzmb.ui.activities.fragments.NouveauRechercheCars
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.helper.SearchViewInterface
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.helper.SuiviVoitureHelper
import com.example.sayaradzmb.model.Modele
import com.example.sayaradzmb.model.Version
import com.example.sayaradzmb.servics.ModeleService
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.servics.VersionService
import com.pusher.pushnotifications.PushNotifications
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModeleAdapter(
    private val modeleList: ArrayList<Modele>,
    internal var context: Context,

    internal var view : View,
    private var modeleListFiltree : ArrayList<Modele>,
    private val activity : FragmentActivity

) : RecyclerView.Adapter<ModeleAdapter.ModeleViewHolder>(), RecycleViewHelper,Filterable,SearchViewInterface,SharedPreferenceInterface,SuiviVoitureHelper {


    var versionDropDown = view.findViewById<ExpandableCardView>(R.id.fnt_ecv_version)

    var modeleDropDown = view.findViewById<ExpandableCardView>(R.id.fnt_ecv_modele)
    private var versionAdapter : VersionAdapter? = null
    private var versionList = ArrayList<Version>()
    private var comm : ((Version) -> Unit)? = null

    private var currentCodeModele : Int = -1
    private var search = view.findViewById<Button>(R.id.search_button)
    @SuppressLint("UseSparseArrays")
    private var modeleVersions = HashMap<Int,ArrayList<Version>>()
    private var frag = 1



    /**
     * Second constructor
     */

    constructor(modeleList: ArrayList<Modele>, context: Context, view : View,  modeleListFiltree: ArrayList<Modele>,
                activity: FragmentActivity, listener: (Version) -> Unit) : this(modeleList,context,view,modeleListFiltree,activity) {
        this.comm = listener
        frag = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModeleViewHolder {
        //inflate the layout file
        val modeleView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_modele, parent, false)
        return ModeleViewHolder(modeleView)
    }

    override fun onBindViewHolder(holder: ModeleViewHolder, position: Int) {

        val modele = modeleListFiltree.get(position)
        val codeMarque = modele.CodeMarque
        var imageSuivi = holder.suivieImage
        holder.nomModele.text = modele.NomModele
        toggleSuivi(modele.suivie,imageSuivi,R.drawable.star,R.drawable.star_vide)
        holder.nomModele.setOnClickListener(View.OnClickListener {

            currentCodeModele = modele.CodeModele!!
            modeleDropDown.setTitle(modele.NomModele)
            modeleDropDown.collapse()
            versionDropDown.setTitle("Version")

            when (frag)
            {
                1 -> {search.visibility=View.GONE
                    versionDropDown.visibility=View.VISIBLE
                    versionDropDown.collapse()
                    init(view)}

                0 -> {
                    versionDropDown.setOnClickListener {
                        if (versionDropDown.isExpanded) versionDropDown.collapse()
                        else versionDropDown.expand()
                    }
                  versionAdapter = VersionAdapter(versionList,view.context,view,versionList,comm!!)
                    initLineaire(view,R.id.imd_rv_version, LinearLayoutManager.VERTICAL,versionAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
                }
            }
            requeteVersion(codeMarque!!,"")
            initSearchView(activity!!,view,versionAdapter!!,R.id.search_bar_version)
        })
        imageSuivi.setOnClickListener {
            println("tag : "+imageSuivi.tag)
            if (imageSuivi.tag == "nonSuivi"){

                /**
                 * faire l'abonnement
                 */
                val vService =  ServiceBuilder.buildService(ModeleService::class.java)
                val requeteAppel = vService.suivreModeles(modele.CodeModele!!,avoirInfoUser(this.context))
                requeteAppel.enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>): Unit =
                        if(response.isSuccessful){
                            println("apres abonnemet : "+response.body().toString())
                            requeteVersion(codeMarque!!,"s")
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
                val vService =  ServiceBuilder.buildService(ModeleService::class.java)
                val requeteAppel = vService.desuivreModele(modele.CodeModele!!,avoirIdUser(this.context)!!)
                requeteAppel.enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>): Unit =
                        if(response.isSuccessful){
                            println("apres Desabonnement : "+response.body().toString())
                            requeteVersion(codeMarque!!,"ns")
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
        return modeleListFiltree.size
    }
    fun addAllwithclear(modeleLists : ArrayList<Modele>){
        modeleList.addAll(modeleLists)
        notifyDataSetChanged()
    }
    fun clear(){
        modeleList.clear()
        notifyDataSetChanged()
    }
    inner class ModeleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var item = view.findViewById<LinearLayout>(R.id.item_modele)
        internal var suivieImage = view.findViewById<ImageView>(R.id.im_image_suivimodele)
        internal var nomModele = view.findViewById<TextView>(R.id.im_text_nommodele)
    }

    /**
     * les methide des requete
     */

    private fun init(v : View){
        versionAdapter = VersionAdapter(versionList,v.context,view,versionList)
        initLineaire(v,R.id.imd_rv_version, LinearLayoutManager.VERTICAL,versionAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }

    private fun requeteVersion(codeMarque : Int,suivi : String){
        versionList.clear()
        var progress = ProgressDialog(context,android.R.style.Theme_DeviceDefault_Dialog)
        progress.setCancelable(false)
        progress.setTitle("charger les Version")
        progress.show()
        val vService =  ServiceBuilder.buildService(VersionService::class.java)
        val requeteAppel = vService.getVersions(avoirIdUser(this.context)!!,currentCodeModele)
        requeteAppel.enqueue(object : Callback<List<Version>> {
            override fun onResponse(call: Call<List<Version>>, response: Response<List<Version>>) =
                if(response.isSuccessful){
                    println("mes version")
                    print(response.body()!!)
                    var lesVersion = response.body()!!
                    lesVersion.forEach{
                            e->
                        if (suivi == "s") PushNotifications.addDeviceInterest("VERSION_${e.CodeVersion}")
                        else if (suivi == "ns") PushNotifications.removeDeviceInterest("VERSION_${e.CodeVersion}")
                        Log.i("codeMarque",codeMarque.toString())
                        e.CodeMarque = codeMarque
                        versionList.add(e)
                    }
                    // avoir la liste des version de modele clique
                    modeleVersions.put(currentCodeModele,versionList)
                    print("allez")
                    progress.dismiss()
                }else{

                }
            override fun onFailure(call: Call<List<Version>>, t: Throwable) {
                Log.w("failConnexion","la liste version non reconnue")
                progress.dismiss()
                requeteVersion(codeMarque,suivi)
            }
        })
    }

    public override fun getFilter(): Filter {
        return object : Filter() {
            protected override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    modeleListFiltree= modeleList
                    Log.i("marquefiltree",modeleListFiltree.toString())
                } else {
                    val filteredList = ArrayList<Modele>()
                    for (row in modeleList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.NomModele?.toLowerCase()?.contains(charString.toLowerCase())!!) {
                            filteredList.add(row)
                        }
                    }
                    modeleListFiltree = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = modeleListFiltree
                return filterResults
            }

             override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                modeleListFiltree = filterResults.values as ArrayList<Modele>
                notifyDataSetChanged()
            }
        }
    }
}
