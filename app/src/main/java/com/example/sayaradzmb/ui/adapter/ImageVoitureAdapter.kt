package com.example.sayaradzmb.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.CheminImage
import com.squareup.picasso.Picasso


class ImageVoitureAdapter(
    private val imageList: ArrayList<CheminImage>?,
    internal var context: Context
) : RecyclerView.Adapter<ImageVoitureAdapter.ImageVoitureViewHolder>() {

    private var currentImage :String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageVoitureViewHolder {
        //inflate the layout file
        val imageVoitureView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image_voiture, parent, false)
        return ImageVoitureViewHolder(imageVoitureView)
    }

    override fun onBindViewHolder(holder: ImageVoitureViewHolder, position: Int) {
        currentImage = imageList!![position].CheminImage
        Picasso.get().load(currentImage).resize(500,200).into(holder.imageview)
        holder.imageview.setOnClickListener {
            println("position $position")
        }
    }

    override fun getItemCount(): Int {
        return imageList!!.size
    }

    inner class ImageVoitureViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        internal var imageview: ImageView = view.findViewById(R.id.item_image_voiture_pic)

    }
}
