package com.example.sayaradzmb.helper

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import java.util.*
import kotlin.collections.ArrayList as ArrayList1

interface RecycleViewHelper {

    /**
     * la fonction qui initilaise le recycleview
     */
    fun initLineaire(v: View,idRV : Int,orientationLayout:Int,adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>?){
        val itemRecycleView = v.findViewById(idRV) as RecyclerView
        val horizontalLayoutManager = LinearLayoutManager(v.context, orientationLayout, false)
        itemRecycleView.layoutManager = horizontalLayoutManager
        itemRecycleView.adapter = adapter!!
    }




}