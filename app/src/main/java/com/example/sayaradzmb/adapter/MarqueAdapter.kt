package com.example.sayaradzmb.adapter

import android.content.Context
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
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.model.Modele
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.servics.ViheculeService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarqueAdapter(
    private val marqueList: ArrayList<Marque>,
    internal var context: Context,
    internal var view : View,
    private var onSearchPressed : NouveauRechercheCars.OnSearchPressed?
) : RecyclerView.Adapter<MarqueAdapter.MarqueViewHolder>(), RecycleViewHelper {
    var marqueDropDown = view.findViewById<ExpandableCardView>(R.id.fnt_ecv_marque)
    var modeleDropDown = view.findViewById<ExpandableCardView>(R.id.fnt_ecv_modele)
    var versionDropDown = view.findViewById<ExpandableCardView>(R.id.fnt_ecv_version)
    private var modeleAdapter : ModeleAdapter? = null
    private var modeleList = ArrayList<Modele>()
    override var itemRecycleView : RecyclerView? = null
    private var currentCodeMarque : Int = -1
    var search = view.findViewById<Button>(R.id.search_button)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarqueViewHolder {
        //inflate the layout file
        val marqueView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_marque, parent, false)
        return MarqueViewHolder(marqueView)
    }

    override fun onBindViewHolder(holder: MarqueViewHolder, position: Int) {
        val marque = marqueList.get(position)
        holder.nomMarque.text = marqueList[position].NomMarque

        if(marque.images!!.isNotEmpty()){
            Log.i("image",marque.images[0].CheminImage.toString())
            Picasso.get().load(marque.images[0].CheminImage).into(holder.logoImage)
        }
        holder.item.setOnClickListener(View.OnClickListener {
            Log.i("marque",marque.CodeMarque.toString())
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
        })
    }

    override fun getItemCount(): Int {
        return marqueList.size
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
        modeleAdapter = ModeleAdapter(modeleList,v.context,view,onSearchPressed)
        initLineaire(v,R.id.imd_rv_modele, LinearLayoutManager.VERTICAL,modeleAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }

    private fun requeteModele(){
        modeleList.clear()
        val vService =  ServiceBuilder.buildService(ViheculeService::class.java)
        val requeteAppel = vService.getModeles(currentCodeMarque)
        requeteAppel.enqueue(object : Callback<List<Modele>> {
            override fun onResponse(call: Call<List<Modele>>, response: Response<List<Modele>>) =
                if(response.isSuccessful){
                    println("mes modeles")
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

}