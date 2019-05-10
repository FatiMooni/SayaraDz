package com.example.sayaradzmb.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.cheminImage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.vehicule_image_display.view.*

class VehiculeImageAdapter : PagerAdapter {
    var context : Context
    var images : List <cheminImage>
    lateinit var layout : LayoutInflater

    constructor(context : Context ,  images : List<cheminImage>):super(){
        this.context = context
        this.images = images
    }

    //redifining the functions
    override fun isViewFromObject(p0: View, p1: Any): Boolean = (p0 == p1 as ConstraintLayout)

    override fun getCount(): Int = images.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
          var img : ImageView

        //creer un layout inflater pour binder data
          layout = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
          var view : View = layout.inflate(R.layout.vehicule_image_display, container, false)

        //cree une instance de la vue

        //binder l'mage dans imageviewer
          img = view.findViewById(R.id.car_image_holder)
          Picasso.get().load(images[position].CheminImage).into(img)
          container.addView(view)
          return  view

    }


}