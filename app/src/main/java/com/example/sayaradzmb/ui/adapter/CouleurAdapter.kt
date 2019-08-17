package com.example.sayaradzmb.ui.adapter

import android.content.Context
import android.content.SyncStatusObserver
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
import com.example.sayaradzmb.ui.activities.CompositionActivity

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
     * Private varibale
     */
    private var view : View?=null
    private var buttonColor :Button?=null

    /**
     * observer
     */
    private var imageObserver : ImageVoitureAdapter?=null
    private var compoitionObsrver : CompositionActivity?=null

    /**
     * change in observer
     */
    private var imagePhoto  = ArrayList<String>()
    private var currentCouleur  : String ? =null

    /**
     * on create view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouleurViewHolder {
        //inflate the layout file
        val couleurView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_couleur, parent, false)
        view=couleurView
        return CouleurViewHolder(couleurView)
    }

    /**
     * le bind view
     */
    override fun onBindViewHolder(holder: CouleurViewHolder, position: Int) {
        val couleur = couleurList[position]
        println(couleur)

        val imageVoiture = couleur.CheminImage
        buttonColor = holder.couleurview
        var color = couleur.CodeHexa!!
        var drawable = buttonColor!!.background
        if(drawable is GradientDrawable){
            drawable.setColor(Color.parseColor(color))
        }
        /**
         * quand on est a l'image 0
         */
        if (position == 0){
            imagePhoto.clear()
            imagePhoto.add(imageVoiture!!)
            setImages(imagePhoto)
            Log.i("image dans couleur : ", imagePhoto[0])

        }
        /**
         * quand on click sur une couleur
         */
        holder.couleurview.setOnClickListener {
            currentCouleur = couleur.CodeHexa
            imagePhoto.clear()
            imagePhoto.add(imageVoiture!!)
            setImages(imagePhoto)
            Log.i("image dans couleur : ", imagePhoto[0])
            notifyComposition(currentCouleur!!)
        }
    }

    override fun getItemCount(): Int {
        return couleurList.size
    }

    inner class CouleurViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var couleurview: Button = view.findViewById(R.id.item_couleur_button)

    }

    /**
     * get ImagePhoto
     */
    fun getImages() : ArrayList<String>?{
        return this.imagePhoto
    }

    /**
     * set imagePhoto
     */
    fun setImages(images : ArrayList<String>){
        this.imagePhoto = images
        notifyObserver()
    }

    /**
     * attach an observer
     */
    fun attach(imageVoitureAdapter: ImageVoitureAdapter){
        this.imageObserver = imageVoitureAdapter
    }

    fun attachComposition(composition :CompositionActivity){
        this.compoitionObsrver = composition
    }
    /**
     * notify the observer
     */
    private fun notifyObserver() {
        imageObserver!!.update()
    }

    private fun notifyComposition(couleur : String){
        this.compoitionObsrver!!.update(couleur)
    }



}
