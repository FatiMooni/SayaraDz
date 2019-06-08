package com.example.sayaradzmb.adapter

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
import com.example.sayaradzmb.activities.Fragments.NouveauRechercheCars
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.helper.SearchViewInterface
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.model.Modele
import com.example.sayaradzmb.model.version
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.servics.ViheculeService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModeleAdapter(
    private val modeleList: ArrayList<Modele>,
    internal var context: Context,
    internal var view : View,
    private var onSearchPressed : NouveauRechercheCars.OnSearchPressed?,
    private var modeleListFiltree : ArrayList<Modele>,
    private val activity : FragmentActivity

) : RecyclerView.Adapter<ModeleAdapter.ModeleViewHolder>(), RecycleViewHelper,Filterable,SearchViewInterface {

    var versionDropDown = view.findViewById<ExpandableCardView>(R.id.fnt_ecv_version)
    var modeleDropDown = view.findViewById<ExpandableCardView>(R.id.fnt_ecv_modele)
    private var versionAdapter : VersionAdapter? = null
    private var versionList = ArrayList<version>()
    override var itemRecycleView : RecyclerView? = null
    private var currentCodeModele : Int = -1
    var search = view.findViewById<Button>(R.id.search_button)
    var modeleVersions = HashMap<Int,ArrayList<version>>()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModeleViewHolder {
        //inflate the layout file
        val modeleView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_modele, parent, false)
        return ModeleViewHolder(modeleView)
    }

    override fun onBindViewHolder(holder: ModeleViewHolder, position: Int) {

        val modele = modeleListFiltree.get(position)
        var imageSuivi = holder.suivieImage
        holder.nomModele.text = modele.NomModele
        holder.nomModele.setOnClickListener(View.OnClickListener {
            currentCodeModele = modele.CodeModele!!
            search.visibility=View.GONE
            modeleDropDown.setTitle(modele.NomModele)
            modeleDropDown.collapse()
            versionDropDown.visibility=View.VISIBLE
            versionDropDown.collapse()
            versionDropDown.setTitle("Version")
            init(view)
            requeteVersion()
            initSearchView(activity!!,view,versionAdapter!!,R.id.search_bar_version)
        })
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
        versionAdapter = VersionAdapter(versionList,v.context,view,onSearchPressed,versionList)
        initLineaire(v,R.id.imd_rv_version, LinearLayoutManager.VERTICAL,versionAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }

    private fun requeteVersion(){
        versionList.clear()
        val vService =  ServiceBuilder.buildService(ViheculeService::class.java)
        val requeteAppel = vService.getVersions(currentCodeModele)
        requeteAppel.enqueue(object : Callback<List<version>> {
            override fun onResponse(call: Call<List<version>>, response: Response<List<version>>) =
                if(response.isSuccessful){
                    println("mes version")
                    print(response.body()!!)
                    var lesVersion = response.body()!!
                    lesVersion.forEach{
                            e->versionList.add(e)
                    }
                    // avoir la liste des version de modele clique
                    modeleVersions.put(currentCodeModele,versionList)
                    print("allez")
                }else{

                }
            override fun onFailure(call: Call<List<version>>, t: Throwable) {
                Log.w("failConnexion","la liste version non reconnue")
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

            protected override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                modeleListFiltree = filterResults.values as ArrayList<Modele>
                notifyDataSetChanged()
            }
        }
    }
}
