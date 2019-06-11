package com.example.sayaradzmb.helper

import android.widget.ImageView
import com.example.sayaradzmb.R
import com.squareup.picasso.Picasso

interface SuiviVoitureHelper {
    fun processusSuivre(drawable :Int, image : ImageView, tag : String){
        Picasso.get().load(drawable).into(image)
        image.tag = tag
    }

    fun toggleSuivi(suivi : Boolean,imageSuivi : ImageView,drawableSuivi : Int,drawableNonSuivi : Int){
        if (suivi){
            processusSuivre(R.drawable.star,imageSuivi,"Suivi")
        }else{
            processusSuivre(R.drawable.star_vide,imageSuivi,"nonSuivi")
        }
    }
}