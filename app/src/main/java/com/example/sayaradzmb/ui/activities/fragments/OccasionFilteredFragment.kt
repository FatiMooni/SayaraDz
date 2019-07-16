package com.example.sayaradzmb.ui.activities.fragments

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.chip.Chip
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.customs.CustomFiltersInitializer
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.model.VehiculeRechFilters
import com.example.sayaradzmb.ui.adapter.OccasionCarsAdapter
import com.example.sayaradzmb.viewmodel.OccasionViewModel

class OccasionFilteredFragment : SharedPreferenceInterface, CustomFiltersInitializer, Fragment() {

    override var typeCarburant: String? = null
    override var maxYear: Int? = null
    override var minYear: Int? = null
    override var maxPrix: Int? = null
    override var minPrix: Int? = null
    override var maxKm: Int? = null

    /**
     * Local Variables
     */

    private var maView: View? = null
    private var filter: VehiculeRechFilters? = null
    private var versionInfo: TextView? = null
    private var idUser: String? = null
    private var annonceList = ArrayList<VehiculeOccasion>()
    private var annonceAdapter: OccasionCarsAdapter? = null
    private var model = OccasionViewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Get the view
        maView = inflater.inflate(R.layout.fragment_occasion_filtered, container, false)

        //Get the current user inforamtions
        idUser = avoirIdUser(maView!!.context).toString()

        //Get the filters from the previous activity
        filter = arguments!!.getParcelable("filters") as VehiculeRechFilters
        val title = arguments!!.getCharSequence("version")


        versionInfo = maView!!.findViewById(R.id.car_info)
        versionInfo!!.text = title

        Toast.makeText(maView!!.context, filter.toString(), Toast.LENGTH_LONG).show()

        prepareRecyclerView(maView!!)
        // Load our BooksViewModel or create a new one
        model = ViewModelProviders.of(this).get(OccasionViewModel::class.java)
        // Listen for changes on the BooksViewModel
        model.getUsedCars().observe(this, Observer<List<VehiculeOccasion>> {

            annonceList.addAll(it!!)
            annonceAdapter!!.notifyDataSetChanged()

        })

        model.loadUsedCars(idUser!!, filters = filter)


        setFiltersCard(filter!!)

        return maView
    }

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private fun setFiltersCard(item: VehiculeRechFilters) {

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

            priceProp.text = item.minPrix!!.div(10000).toString() + "-" +
                    item.maxPrix!!.div(10000).toString()
                        .plus(" M ")
                        .plus(context!!.resources.getString(R.string.price_sign))

        } else {
            priceProp.text = "Any Price"
        }

        //The Maximum distance
        if (item.maxKm != null) {
            kmProp.text = item.maxKm.toString() + context!!.resources.getString(R.string.distance_sign)
        } else {
            kmProp.text = "Any Distance"
        }

        //The year of matriculation
        if (item.maxAnnee != null || item.minAnnee != null) {

            yearProp.text = item.minAnnee.toString() + "-" + item.maxAnnee.toString()
        } else {
            yearProp.text = "Any Year"
        }

        //The used Fuel
        if (item.carburant != null) {

            fuelProp.text = item.carburant
        } else {
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
            model.loadUsedCars(idUser!!, filter)


        }

        fuelProp.setOnCloseIconClickListener {
            fuelProp.visibility = View.GONE
            filter!!.carburant = null
            model.loadUsedCars(idUser!!, filter)

        }

        kmProp.setOnCloseIconClickListener {
            kmProp.visibility = View.GONE
            filter!!.maxKm = null
            model.loadUsedCars(idUser!!, filter)


        }

        priceProp.setOnCloseIconClickListener {
            priceProp.visibility = View.GONE
            filter!!.maxPrix = null
            filter!!.minPrix = null

            model.loadUsedCars(idUser!!, filter)


        }


    }

    /**
     * To edit the filters directly
     */
    private fun showPriceChooser() {
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
            Toast.makeText(maView!!.context, minPrix.toString(), Toast.LENGTH_LONG).show()
            model.loadUsedCars(idUser!!, filter)

            setFiltersCard(filter!!)

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showFuelChooser() {

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
            Toast.makeText(maView!!.context, typeCarburant, Toast.LENGTH_LONG).show()
            model.loadUsedCars(idUser!!, filters = filter)

            setFiltersCard(filter!!)

        }
    }

    private fun showDistanceChooser() {
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
            Toast.makeText(maView!!.context, maxKm.toString(), Toast.LENGTH_LONG).show()
            model.loadUsedCars(idUser!!, filter)

            setFiltersCard(filter!!)
        }
    }


    private fun showYearChooser() {
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
            Toast.makeText(maView!!.context, maxYear.toString(), Toast.LENGTH_LONG).show()
            model.loadUsedCars(idUser!!, filter)

            setFiltersCard(filter!!)

        }
    }

    private fun prepareRecyclerView(v: View) {
        val layout = LinearLayoutManager(v.context)
        layout.orientation = LinearLayoutManager.VERTICAL
        val adapter = v.findViewById<RecyclerView>(R.id.recyler_view_voiture_occasion)
        adapter.layoutManager = layout
        annonceAdapter = OccasionCarsAdapter(v.context, annonceList)
        adapter.adapter = annonceAdapter
    }

}