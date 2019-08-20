package com.example.sayaradzmb.ui.adapter

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Vehicule
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.ui.adapter.viewholders.BaseViewHolder
import com.example.sayaradzmb.ui.adapter.viewholders.LastItemViewHolder
import com.example.sayaradzmb.ui.adapter.viewholders.NewCarsViewHolder
import com.example.sayaradzmb.ui.adapter.viewholders.UsedCarsViewHolder

class AcceuilleCardAdapter(val context: Context, var data: ArrayList<Comparable<*>>) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {
    companion object {
        const val TYPE_ITEM_OCCASION = 0
        const val TYPE_ITEM_NEW = 1
        const val TYPE_LAST = 2
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return if (viewType == TYPE_LAST) {
            val view = LayoutInflater.from(context).inflate(R.layout.add_card_view, p0, false)
            LastItemViewHolder(view, listener)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.main_card_view, p0, false)
            when (viewType) {
                TYPE_ITEM_NEW -> NewCarsViewHolder(view)
                TYPE_ITEM_OCCASION -> UsedCarsViewHolder(view)
                else -> throw IllegalArgumentException("Invalid type of data $viewType")
            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        return if (position == data.size) TYPE_LAST
        else when (data[position]) {
            is VehiculeOccasion -> TYPE_ITEM_OCCASION
            is Int -> TYPE_ITEM_NEW
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if(data.size == position ) {
           if(data.isNotEmpty()) (holder as LastItemViewHolder).bind(data[position-1])
        } else
        {   val element = data[position]
            when(holder){
                is UsedCarsViewHolder -> holder.bind(element as VehiculeOccasion)
                is NewCarsViewHolder -> holder.bind(element as Int)
                else -> throw IllegalArgumentException("Invalid type of holder $position")
            }
        }

    }
    init {
        data = ArrayList()
    }

    fun swapData(newData: List<Comparable<*>>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    interface OnCardButtonListener{
        fun OnCardButton(item : Comparable<*>? , itemType : Int)
    }
    private  var listener : OnCardButtonListener? = null

    fun  setOnCardButtonClickListener(listener: OnCardButtonListener){
        this.listener = listener
    }

}