package com.example.sayaradzmb.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Option
import com.example.sayaradzmb.ui.activities.CompositionActivity

/**
 * Option Type :
 *  0 => checkbox
 *  0 => List text
 */
class OptionAdapter(
    private val optionList: ArrayList<Option>,
    private var context: Context,
    private val typeOption: Int
) : RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {

    /**
     * private Variable
     */
    private var view: View? = null

    /**
     * observer
     */
    private var compoitionObsrver: CompositionActivity? = null

    /**
     * change in observer
     */
    private var currentOptions = ArrayList<Int>()

    /**
     * On create variable
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        var optionView: View? = null
        //inflate the layout file
        if (typeOption == 0) {
            optionView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_checkbox, parent, false)
        } else {
            optionView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_option_list, parent, false)
        }
        view = optionView
        return OptionViewHolder(optionView, typeOption)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val option = optionList[position]
        if (typeOption == 0) {
            val optionHoler = holder.optionview
            optionHoler!!.text = option.NomOption
            optionHoler.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    currentOptions.add(option.CodeOption!!)
                } else {
                    currentOptions.remove(option.CodeOption!!)
                }
                notifyComposition(currentOptions)
            }
        } else {
            val optionHolder = holder.optionviewText
            optionHolder!!.text = option.NomOption
        }
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    inner class OptionViewHolder(view: View, typeOption: Int) : RecyclerView.ViewHolder(view) {
        internal var optionview: CheckBox? = null
        internal var optionviewText: TextView? = null

        init {
            if (typeOption == 0) {
                optionview = view.findViewById(R.id.option_checkbox)
            } else {
                optionviewText = view.findViewById(R.id.item_option_text)
            }
        }

    }

    /**
     * attach an observer
     */
    fun attachComposition(composition: CompositionActivity) {
        this.compoitionObsrver = composition
    }

    /**
     * notify the observer
     */
    private fun notifyComposition(optionList: ArrayList<Int>) {
        this.compoitionObsrver!!.updateOption(optionList)
    }
}