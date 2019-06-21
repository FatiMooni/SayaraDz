package com.example.sayaradzmb.ui.activities.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.VehiculeRechFilters

class OccasionFilteredFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val maView = inflater.inflate(R.layout.fragment_occasion_filtered, container, false)
        val bundle = Bundle()
        val filter = bundle.getParcelable<VehiculeRechFilters>("filters")

        Toast.makeText(maView.context,filter!!.toString(),Toast.LENGTH_LONG).show()
        return maView
    }

}