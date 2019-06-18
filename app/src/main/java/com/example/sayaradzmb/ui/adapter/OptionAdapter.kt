package com.example.sayaradzmb.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Option

class OptionAdapter(
    private val optionList: ArrayList<Option>,
    private var context: Context
) : RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {

    /**
     * Declaration
     */
    private var view : View?=null

    /**
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        //inflate the layout file
        val optionView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_checkbox, parent, false)
        view=optionView
        return OptionViewHolder(optionView)
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val option = optionList[position]
        val optionHoler = holder.optionview
        optionHoler.text = option.NomOption
        optionHoler.isChecked = true

    }

    /**
     *
     */
    override fun getItemCount(): Int {
        return optionList.size
    }

    /**
     *
     */
    inner class OptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var optionview: CheckBox = view.findViewById(R.id.option_checkbox)
    }
}