package com.example.sayaradzmb.helper

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log

class ColorHelper{
    companion object{
        fun colorerButton(drawable : GradientDrawable,color: String){
            drawable.setColor(Color.parseColor(color))
        }
    }
}