package com.example.sayaradzmb.ui.activities.fragments

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.chip.Chip
import android.support.design.chip.ChipGroup
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.customs.CustomFiltersInitializer
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.VehiculeRechFilters

class OccasionFilteredFragment : CustomOccasionFragment(),SharedPreferenceInterface,CustomFiltersInitializer {

    override var typeCarburant : String = ""
    override var maxYear : Int = 0
    override var minYear : Int = 0
    override var maxPrix : Int = 0
    override var minPrix : Int = 0
    override var maxKm : Int = 0
    /**
     * Local Variables
     */

    private var maView : View? = null
    private var filter : VehiculeRechFilters? = null
    private var versionInfo : TextView? = null
    var idUser : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //
        maView = inflater.inflate(R.layout.fragment_occasion_filtered, container, false)

        idUser = avoirIdUser(maView!!.context).toString()

        filter = arguments!!.getParcelable("filters") as VehiculeRechFilters
        val title = arguments!!.getCharSequence("version")

        versionInfo = maView!!.findViewById(R.id.car_info)
        versionInfo!!.text = title

        Toast.makeText(maView!!.context,filter.toString(),Toast.LENGTH_LONG).show()

        prepareRecyclerView(maView!!)
        getAnnonceList(idUser!!,maView = maView!! , filters = filter)
        setFiltersCard(filter!!)

        return maView
    }

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private fun setFiltersCard(item : VehiculeRechFilters){

        //getting the chips card
        val kmProp = maView!!.findViewById<Chip>(R.id.max_km)
        val priceProp = maView!!.findViewById<Chip>(R.id.price_range)
        val yearProp = maView!!.findViewById<Chip>(R.id.years_range)
        val fuelProp = maView!!.findViewById<Chip>(R.id.type_carb)

        /**
         * Set the properties
         */

        //The Price
        if (item.maxPrix != null || item.minPrix != null) {
            priceProp.text = item.minPrix!!.div( 1000000).toString() +"-"+
                    item.maxPrix!!.div( 1000000).toString() +
                    " M " + context!!.resources.getString(R.string.price_sign)

        }
        else {
            priceProp.text = "Any Price"
        }

        //The Maximum distance
        if (item.maxKm != null) {
            kmProp.text = item.maxKm.toString() + context!!.resources.getString(R.string.distance_sign)
        }
        else {
            kmProp.text = "Any Distance"
        }

        //The year of matriculation
        if (item.maxAnnee != null || item.minAnnee!= null) {

            yearProp.text = item.minAnnee.toString() + "-" + item.maxAnnee.toString()
        }
        else {
            yearProp.text = "Any Year"
        }

        //The used Fuel
        if (item.carburant != null ) {

            fuelProp.text = item.carburant
        }
        else {
            fuelProp.text = "Any Type"
        }


        /**
         * Actions : Edit the filters
         */
        kmProp.setOnClickListener {
            showDistanceChooser()
        }

        priceProp.setOnClickListener {
            showPriceChooser()
        }

        fuelProp.setOnClickListener {
            showFuelChooser()
        }
        yearProp.setOnClickListener {
            showYearChooser()
        }

        /**
         * Action : Deleting a filter
         */
        yearProp.setOnCloseIconClickListener {
            yearProp.visibility = View.GONE
            filter!!.maxAnnee = null
            filter!!.minAnnee = null
            getAnnonceList(idUser!!,maView = maView!! , filters = filter)

        }

        fuelProp.setOnCloseIconClickListener {
            fuelProp.visibility = View.GONE
            filter!!.carburant = null
            getAnnonceList(idUser!!,maView = maView!! , filters = filter)
        }

        kmProp.setOnCloseIconClickListener {
            kmProp.visibility = View.GONE
            filter!!.maxKm = null
            getAnnonceList(idUser!!,maView = maView!! , filters = filter)

        }

        priceProp.setOnCloseIconClickListener {
            priceProp.visibility = View.GONE
            filter!!.maxPrix = null
            filter!!.minPrix = null
            getAnnonceList(idUser!!,maView = maView!! , filters = filter)

        }


    }

    /**
     * To edit the filters directly
     */
    fun showPriceChooser(){
        val mDialogView = LayoutInflater.from(maView!!.context).inflate(R.layout.price_chooser, null)

        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(maView!!.context)
            .setView(mDialogView)
            .setTitle("Price Range")

        //afficher le dialog
        val chooser = mBuilder.create()
        chooser.show()

        initPriceRange(mDialogView)

        chooser.setOnDismissListener {
            filter!!.minPrix = minPrix
            filter!!.maxPrix = maxPrix
            Toast.makeText(maView!!.context, minPrix.toString() , Toast.LENGTH_LONG).show()
            getAnnonceList(idUser!!,maView = maView!! , filters = filter)
            setFiltersCard(filter!!)

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun showFuelChooser(){
        val mDialogView = LayoutInflater.from(maView!!.context).inflate(R.layout.fuel_chooser, null)

        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(maView!!.context)
            .setView(mDialogView)
            .setTitle("Fuel Type")

        //afficher le dialog
        val chooser = mBuilder.create()
        chooser.show()


        initFeaturesLayout(mDialogView, LayoutInflater.from(maView!!.context))

        chooser.setOnDismissListener {
            filter!!.carburant = typeCarburant
            Toast.makeText(maView!!.context, typeCarburant , Toast.LENGTH_LONG).show()
            getAnnonceList(idUser!!,maView = maView!! , filters = filter)
            setFiltersCard(filter!!)

        }
    }
    fun showDistanceChooser(){
        val mDialogView = LayoutInflater.from(maView!!.context).inflate(R.layout.km_chooser, null)

        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(maView!!.context)
            .setView(mDialogView)
            .setTitle("Distance Range")

        //afficher le dialog
        val chooser = mBuilder.create()
        chooser.show()

        initKmRange(mDialogView)

        chooser.setOnDismissListener {
            filter!!.maxKm = maxKm
            Toast.makeText(maView!!.context, maxKm.toString() , Toast.LENGTH_LONG).show()
            getAnnonceList(idUser!!,maView = maView!! , filters = filter)
            setFiltersCard(filter!!)
        }
    }


    fun showYearChooser(){
        val mDialogView = LayoutInflater.from(maView!!.context).inflate(R.layout.years_chooser, null)

        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(maView!!.context)
            .setView(mDialogView)
            .setTitle("Years Range")

        //afficher le dialog
        val chooser = mBuilder.create()
        chooser.show()

        initYearsSpinner(mDialogView)

        chooser.setOnDismissListener {
            filter!!.maxAnnee = maxYear
            filter!!.minAnnee = minYear
            Toast.makeText(maView!!.context, maxYear.toString() , Toast.LENGTH_LONG).show()
            getAnnonceList(idUser!!,maView = maView!! , filters = filter)
            setFiltersCard(filter!!)

        }
    }

}