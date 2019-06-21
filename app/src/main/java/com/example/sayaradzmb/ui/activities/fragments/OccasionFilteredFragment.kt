package com.example.sayaradzmb.ui.activities.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.chip.Chip
import android.support.design.chip.ChipGroup
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.VehiculeRechFilters

class OccasionFilteredFragment : CustomOccasionFragment() {

    /**
     * Local Variables
     */

    private var maView : View? = null
    private var versionInfo : TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //
        maView = inflater.inflate(R.layout.fragment_occasion_filtered, container, false)
        val filter = arguments!!.getParcelable("filters") as VehiculeRechFilters

        Toast.makeText(maView!!.context,filter.toString(),Toast.LENGTH_LONG).show()

        prepareRecyclerView(maView!!)
        getAnnonceList("475",maView = maView!! , filters = filter)
        setFiltersCard(filter)

        return maView
    }

    @SuppressLint("SetTextI18n")
    private fun setFiltersCard(item : VehiculeRechFilters){

        //getting the chips card
        val kmProp = maView!!.findViewById<Chip>(R.id.max_km)
        val priceProp = maView!!.findViewById<Chip>(R.id.price_range)
        val yearProp = maView!!.findViewById<Chip>(R.id.years_range)
        val fuelProp = maView!!.findViewById<Chip>(R.id.type_carb)

        //set the properties
        kmProp.text = item.maxKm.toString() + context!!.resources.getString(R.string.kilometrage)
        priceProp.text = item.minPrix.toString() +"-"+ item.maxPrix.toString() + context!!.resources.getString(R.string.price_sign)
        yearProp.text = item.minAnnee.toString() +"-"+ item.maxAnnee.toString()
        fuelProp.text = item.carburant

        //listners
        kmProp.setOnClickListener {

        }

    }

}