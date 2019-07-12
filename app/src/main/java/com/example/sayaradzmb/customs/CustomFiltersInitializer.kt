package com.example.sayaradzmb.customs

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.fuel_type
import com.ianpinto.androidrangeseekbar.rangeseekbar.RangeSeekBar
import java.util.*
import kotlin.collections.ArrayList

interface CustomFiltersInitializer {

    var typeCarburant : String?
    var maxYear : Int?
    var minYear : Int?
    var maxPrix : Int?
    var minPrix : Int?
    var maxKm : Int?

    //Get the fuel type used
    @RequiresApi(Build.VERSION_CODES.M)
    fun initFeaturesLayout(view : View, inf : LayoutInflater){

        val holder = view.findViewById<LinearLayoutCompat>(R.id.carburant_type_holder)
        for (carb in fuel_type) {

            //Inflate the view
            val carburantView = inf.inflate(R.layout.item_carburant, null)


            val txtCarb = carburantView.findViewById<TextView>(R.id.title_carb_type)
            txtCarb.text = carb.title
            carburantView.findViewById<ImageView>(R.id.aprev_carb_type).setImageDrawable(view.context.resources.getDrawable(carb.image,null))

            //Ajouter un type de carburant
            holder.addView(carburantView)

            carburantView.setOnClickListener {
                typeCarburant = carb.title
                txtCarb.setTextColor(view.context.resources.getColor( R.color.colorPrimary, null))
            }


        }
    }

    //Get the range of cars year edition
    fun  initYearsSpinner(view : View){
        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1990..thisYear) {
            years.add(Integer.toString(i))
        }
        val adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_dropdown_item, years)

        val spinYearS = view.findViewById(R.id.years_picker_start) as Spinner
        val spinYearE = view.findViewById(R.id.years_picker_end) as Spinner
        spinYearS.adapter = adapter
        spinYearE.adapter = adapter

        spinYearE.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                minYear = years[position].toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        spinYearS.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                maxYear = years[position].toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

    }

    //Get the range of cars prices
     fun initPriceRange(view : View){
        val priceRange = view.findViewById<RangeSeekBar<Int>>(R.id.price_range)

        priceRange.setOnRangeSeekBarChangeListener { _, minValue, maxValue ->
            maxPrix = maxValue * 10000
            minPrix = minValue * 10000
        }

    }



    //Get the maximum kilometrege
   fun initKmRange(view : View){
        val priceRange = view.findViewById<RangeSeekBar<Int>>(R.id.Km_range)

        priceRange.setOnRangeSeekBarChangeListener { _, _, maxValue ->
            maxKm = (maxValue * 1000)
        }

    }


}
