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
import com.example.sayaradzmb.ui.activities.fragments.NouveauRechercheCars
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.helper.SearchViewInterface
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.model.Modele
import com.example.sayaradzmb.servics.ModeleService
import com.example.sayaradzmb.servics.ServiceBuilder
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MarqueAdapter(
    private var marqueList: ArrayList<Marque>,
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

    //
    var frag = 1

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



   /* constructor(marqueList: ArrayList<Marque>, context: Context, view : View,  onSearchPressed : NouveauRechercheCars.OnSearchPressed?) : this(marqueList,context,view) {
        this.onSearchPressed = onSearchPressed
        frag = 1
    }*/


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
            marqueDropDown.setTitle(marque.NomMarque)
            marqueDropDown.collapse()
            modeleDropDown.setTitle("Modele")


            //settings for each fragment
            when (frag) {
                0 -> {
                    Log.i("occasion",view.context.toString())
                    modeleDropDown.setOnClickListener {
                        if (modeleDropDown.isExpanded) modeleDropDown.collapse()
                        else modeleDropDown.expand()
                    }
                    //modeleAdapter = ModeleAdapter(modeleList,view.context,view)
                    initLineaire(view,R.id.imd_rv_modele, LinearLayoutManager.VERTICAL,modeleAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
                }

                1 -> {
                    search.visibility=View.GONE
                    modeleDropDown.collapse()
                    modeleDropDown.visibility=View.VISIBLE
                    versionDropDown.visibility=View.GONE
                    init(view)
                    if(marqueview == null) Log.i("marqueview","marqueview null")
                    initSearchView(activity,view!!,modeleAdapter!!,R.id.search_bar_modele)
                    }

            }
            requeteModele()


        })

    }

    override fun getItemCount(): Int {
        return marqueListFiltree.size
    }

    fun addAllwithclear(marqueLists : ArrayList<Marque>){
        //marqueList.clear()
        marqueList.addAll(marqueLists)
        notifyDataSetChanged()
    }

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
        val vService =  ServiceBuilder.buildService(ModeleService::class.java)
        val requeteAppel = vService.getModeles(avoirIdUser(this.context),currentCodeMarque)
        requeteAppel.enqueue(object : Callback<List<Modele>> {
            override fun onResponse(call: Call<List<Modele>>, response: Response<List<Modele>>) =
                if(response.isSuccessful){
                    print(response.body()!!)
                    var lesModele = response.body()!!
                    lesModele.forEach{
                            e->modeleList.add(e)
                    }
                }else{

                }
            override fun onFailure(call: Call<List<Modele>>, t: Throwable) {
                Log.w("failConnexion","la liste modele non reconnue ${t.message}")
            }
        })
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
