package com.example.sayaradzmb.activities.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.sayaradzmb.R
import com.example.sayaradzmb.adapter.FollowedCarsCardAdapter
import com.example.sayaradzmb.adapter.NewCarsCardAdapter
import com.example.sayaradzmb.adapter.UsedCarsCardAdapter

class AccuilleFragment : Fragment(){
    private var carsList  = ArrayList<Int>()
    private var usedList  = ArrayList<Int>()
    private var followedList  = ArrayList<Int>()
    private lateinit var activityView: View
    private lateinit var customNewAdapter : NewCarsCardAdapter
    private lateinit var customUsedAdapter : UsedCarsCardAdapter
    private lateinit var customFollowedAdapter : FollowedCarsCardAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        //retourner la vue pour ce fragment
        activityView = inflater.inflate(R.layout.fragement_accuille,null)

        carsList.add(1)
        carsList.add(5)
        carsList.add(5)

        usedList.add(4)
        usedList.add(6)

        // preparer Recycler view
        intialiserNewRecyclerView()
        intialiserUsedRecyclerView()
        intialiserFollowedRecyclerView()


        return  activityView
    }


    private fun intialiserNewRecyclerView() {
        val rv = activityView.findViewById<RecyclerView>(R.id.recyler_view_new)
        val layout = LinearLayoutManager(activityView.context)
        layout.orientation = LinearLayoutManager.HORIZONTAL
        rv.layoutManager = layout
        customNewAdapter =
            NewCarsCardAdapter(activityView.context!!, carsList)
        rv.adapter = customNewAdapter
    }

    private fun intialiserUsedRecyclerView() {
        val rv = activityView.findViewById<RecyclerView>(R.id.recyler_view_anciens)
        val layout = LinearLayoutManager(activityView.context)
        layout.orientation = LinearLayoutManager.HORIZONTAL
        rv.layoutManager = layout
        customUsedAdapter =
            UsedCarsCardAdapter(activityView.context!!, usedList)
        rv.adapter = customUsedAdapter
    }

    private fun intialiserFollowedRecyclerView() {
        val rv = activityView.findViewById<RecyclerView>(R.id.recyler_view_following)
        val layout = LinearLayoutManager(activityView.context)
        layout.orientation = LinearLayoutManager.HORIZONTAL
        rv.layoutManager = layout
        customFollowedAdapter =
            FollowedCarsCardAdapter(activityView.context!!, followedList)
        rv.adapter = customFollowedAdapter
    }



}

