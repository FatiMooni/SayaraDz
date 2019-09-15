package com.example.sayaradzmb.ui.adapter

import android.content.Context
import android.widget.TextView
import com.smarteist.autoimageslider.SliderViewAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.CheminImage
import com.squareup.picasso.Picasso


class CarImageSliderAdapter ( val context: Context , val images : List<CheminImage>) :
    SliderViewAdapter<CarImageSliderAdapter.SliderAdapterVH>() {
    override fun getCount(): Int {
        return  images.size
    }


    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout_item, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
            Picasso.get()
                .load(images[position].CheminImage)
                .error(R.drawable.no_picture)
                .into(viewHolder.imageViewBackground)


    }

  inner class SliderAdapterVH(var itemView: View) :
        SliderViewAdapter.ViewHolder(itemView) {
        var imageViewBackground: ImageView

        init {
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider)
        }
    }
}