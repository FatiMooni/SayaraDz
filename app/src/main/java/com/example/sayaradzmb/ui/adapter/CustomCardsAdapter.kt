package com.example.sayaradzmb.ui.adapter

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Commande
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.model.UserOffre
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.ui.adapter.viewholders.*
import com.facebook.internal.Mutable
import java.util.*
import kotlin.collections.ArrayList

class CustomCardsAdapter(val context: Context, var data: ArrayList<Comparable<*>>) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var onClickItem: OnClickItemListener? = null



    companion object {
        const val TYPE_OFFER = 0
        const val TYPE_OCCASION = 1
        const val TYPE_COMMANDE = 2
        const val TYPE_USEROFFER = 3
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
            is Commande -> TYPE_COMMANDE
            is UserOffre -> TYPE_USEROFFER
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): BaseViewHolder<*> {
        //p0.removeAllViews()
        return when (viewType) {
            TYPE_OCCASION -> {
                val view = LayoutInflater.from(context).inflate(R.layout.used_cars_view, p0, false)
                OccasionCarsViewHolder(view)
            }
            TYPE_OFFER -> {
                val view = LayoutInflater.from(context).inflate(R.layout.offer_owner_card, p0, false)
                OffersViewHolder(view, listener = onClickItem!!)
            }
            TYPE_COMMANDE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.user_commande_card, p0, false)
                CommandeViewHolder(view)
            }
            TYPE_USEROFFER -> {
                val view = LayoutInflater.from(context).inflate(R.layout.user_offer_card, p0, false)
                UserOffersViewHolder(view, onClickItem!!)
            }
            else -> throw IllegalArgumentException("Invalid view type")

        }
    }

    fun getItemAt(position: Int): Comparable<*> {
        return data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = data[position]
        Log.i("came in her ", "everything okay")
        when (holder) {
            is OffersViewHolder -> holder.bind(element as Offre)
            is OccasionCarsViewHolder -> holder.bind(element as VehiculeOccasion)
            is CommandeViewHolder -> holder.bind(element as Commande)
            /**
             * TODO("something")
             * **/
            is UserOffersViewHolder -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.bind(element as UserOffre)
            }
            else -> throw IllegalArgumentException()
        }

    }

    //Pour les action associés aux childView ( Buttons , Menu ... etc)
    interface OnClickItemListener {
        fun onClickItem(id: Int, state: String, position: Int)
        fun onPopupMenuRequested(value : Comparable<*>,view : View, position: Int)
    }



    fun setOnItemClickListener(listener: OnClickItemListener) {
        this.onClickItem = listener
    }





}