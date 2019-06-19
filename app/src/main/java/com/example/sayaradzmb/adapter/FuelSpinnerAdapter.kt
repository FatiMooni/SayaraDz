package com.example.sayaradzmb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Carburant

class FuelSpinnerAdapter (val context: Context, var listItemsTxt: ArrayList<Carburant>) : BaseAdapter() {


    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.carburant_item_spinner, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        // setting adapter item height programatically.

        val params = view.layoutParams
        params.height = 100
        view.layoutParams = params

        vh.label.text = listItemsTxt[position].title
        vh.img.setImageDrawable(view.resources.getDrawable(listItemsTxt[position].image,null))
        return view
    }

    override fun getItem(position: Int): Any? {

        return null

    }

    override fun getItemId(position: Int): Long {

        return 0

    }

    override fun getCount(): Int {
        return listItemsTxt.size
    }

    private class ItemRowHolder(row: View?) {

        val label: TextView
        val img : ImageView
        init {
           label = row?.findViewById(R.id.txtDropDownLabel)!!
            img = row.findViewById(R.id.imgDropDownMenuIcon)!!

        }
    }

}