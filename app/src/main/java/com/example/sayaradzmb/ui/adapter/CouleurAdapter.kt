package com.example.sayaradzmb.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.sayaradzmb.R

import com.example.sayaradzmb.model.Couleur

/**
 *  probleme :
 *  how to have the right color from the backend
 *   1- we should have all the colors in the ressources we shoild talk about it
 */


class CouleurAdapter(
    private val couleurList: ArrayList<Couleur>,
    internal var context: Context
) : RecyclerView.Adapter<CouleurAdapter.CouleurViewHolder>() {
    /**
     * Declaration
     */
    private var view : View?=null
    private var buttonColor :Button?=null

    /**
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouleurViewHolder {
        //inflate the layout file
        val couleurView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_couleur, parent, false)
        view=couleurView
        return CouleurViewHolder(couleurView)
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: CouleurViewHolder, position: Int) {
        buttonColor = view!!.findViewById(R.id.item_couleur_button)
        var color = couleurList[position].CodeHexa!!
        var drawable = buttonColor!!.background
        Log.i("classe name",drawable::class.toString())
        if(drawable is GradientDrawable){
            drawable.setColor(Color.parseColor("#C3C3C3"))
            Log.i("is shape : ", "yes")
        }
        holder.couleurview.setOnClickListener {
            println("position $position")
        }
    }

    /**
     *
     */
    override fun getItemCount(): Int {
        return couleurList.size
    }

    /**
     *
     */
    inner class CouleurViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        internal var couleurview: Button = view.findViewById(R.id.item_couleur_button)

    }
}
