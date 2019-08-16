package com.example.sayaradzmb.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.ui.adapter.viewholders.BaseViewHolder
import com.example.sayaradzmb.ui.adapter.viewholders.OccasionCarsViewHolder
import com.example.sayaradzmb.ui.adapter.viewholders.OffersViewHolder
import java.util.*

class OccasionCarsAdapter(val context: Context, var data: ArrayList<Comparable<*>>) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var onClickItem : OnClickItemListener? = null

    companion object {
      const val TYPE_OFFER = 0
      const val TYPE_OCCASION = 1
    }

    init {
        data = ArrayList()
    }

    fun swapData(newData: List<Comparable<*>>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is Offre -> TYPE_OFFER
            is VehiculeOccasion -> TYPE_OCCASION
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): BaseViewHolder<*> {
        //p0.removeAllViews()
        return when (viewType) {
            TYPE_OCCASION -> {
                val view = LayoutInflater.from(context).inflate(R.layout.used_cars_view, p0, false)
                OccasionCarsViewHolder(view)}
            TYPE_OFFER -> {
                val view = LayoutInflater.from(context).inflate(R.layout.offer_owner_card, p0, false)
                OffersViewHolder(view,listener = onClickItem!! )
            }
            else -> throw IllegalArgumentException("Invalid view type")

        }
    }

    fun getItemAt(position: Int) : Comparable<*>{
        return data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }



    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = data[position]
        Log.i("came in her ", "everything okay")
        when(holder){
            is OffersViewHolder -> holder.bind(element as Offre)
            is OccasionCarsViewHolder -> holder.bind(element as VehiculeOccasion)
            else -> throw IllegalArgumentException()
        }

    }

    interface OnClickItemListener {
       fun onClickItem(id : Int, state : String , position: Int)
    }

    fun SetOnItemClickListener(listener : OnClickItemListener){
        this.onClickItem = listener
    }


    /****
    // class pour binder les information dans les cards view
    inner class ViewHolder (private val objet : View) : RecyclerView.ViewHolder(objet){


    @SuppressLint("SetTextI18n")
    fun bindInfo(item : VehiculeOccasion){
    objet.findViewById<TextView>(R.id.annonce_info).text =
    "${item.version!!.NomMarque} ${item.version!!.NomModele} ${item.version!!.NomVersion}"
    objet.findViewById<TextView>(R.id.annonce_price_info).text = item.Prix
    objet.findViewById<TextView>(R.id.annonce_annee).text = item.Annee
    objet.findViewById<TextView>(R.id.annonce_km).text = item.Km
    objet.findViewById<TextView>(R.id.annonce_fuel).text = item.Carburant
    val pAdapter = VehiculeImageAdapter(context, item.images!!)
    objet.findViewById<ViewPager>(R.id.annonce_image).adapter = pAdapter

    objet.findViewById<Button>(R.id.annonce_details).setOnClickListener {
    // preparé l'activité d'ajout
    val intent = Intent(context, OffreApercuActivity::class.java)
    //Bundle
    val bundle = Bundle()
    bundle.putParcelable("annonce",item)
    intent.putExtra("annonce",item)
    // lancer l'activité
    startActivity(context,intent,null)

    }


    objet.findViewById<Button>(R.id.offer_btn).setOnClickListener {
    val intent = Intent(context, OffreActivity::class.java)
    intent.putExtra("annonce",item)
    // lancer l'activité
    startActivity(context,intent,null)
    }
    }
    }***/
}