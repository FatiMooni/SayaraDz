package com.example.sayaradzmb.ui.activities.fragments


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alespero.expandablecardview.ExpandableCardView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.ui.adapter.MarqueAdapter
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.model.VehiculeRechFilters
import com.example.sayaradzmb.model.fuel_type
import com.example.sayaradzmb.servics.MarqueService
import com.example.sayaradzmb.servics.ServiceBuilder
import com.ianpinto.androidrangeseekbar.rangeseekbar.RangeSeekBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class OccasionRechFragment : Fragment(),RecycleViewHelper {

    override var itemRecycleView : RecyclerView? = null
    private var marqueList = ArrayList<Marque>()
    private var marqueAdapter : MarqueAdapter? = null
    private var featuresDropDown : ExpandableCardView? = null

    private lateinit var item : VehiculeRechFilters

    //variables utilisés dans la requete
    private var nameVersion : String? = null
    private var codeVersion :Int? = null
    private var typeCarburant : String? = null
    private var maxYear : Int? = null
    private var minYear : Int? = null
    private var maxPrix : Int? = null
    private var minPrix : Int? = null
    private var maxKm : Int? = null



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var myView = inflater.inflate(R.layout.fragment_occasion_rech,container,false)

        //prepare the recycle view + marque list to get the code version
        init(myView)
        getMarque()


        //prepare other views to get other filters
        initFeaturesLayout(myView,inflater)
        initYearsSpinner(myView)
        initPriceRange(myView)
        initKmRange(myView)

        val btnSearch = myView.findViewById<Button>(R.id.search_button)

        //Send filters to the next fragment
        val bundle = Bundle()
        bundle.putCharSequence("version", nameVersion)
        btnSearch.setOnClickListener {
            item = VehiculeRechFilters(codeVersion, typeCarburant  , minPrix , maxPrix ,minYear,maxYear,maxKm)
            bundle.putParcelable("filters", item)
            val nextFrag = OccasionFilteredFragment()
            nextFrag.arguments = bundle
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_id, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit()
        }



        //retourner le fragment
        return myView
    }



    /**
     * la fonctin qui va faire l'appel a lapi pour avoir les marque
     */
    private fun getMarque(){
        val vService = ServiceBuilder.buildService(MarqueService::class.java)
        val requeteAppel = vService.getMarques()
        requeteAppel.enqueue(object : Callback<List<Marque>> {
            override fun onResponse(call: Call<List<Marque>>, response: Response<List<Marque>>) =
                if(response.isSuccessful){
                    print(response.body()!!)
                    val lesMarque = response.body()!!
                    lesMarque.forEach{
                            e->marqueList.add(e)
                    }
                }else{

                }
            override fun onFailure(call: Call<List<Marque>>, t: Throwable) {
                Log.w("failConnexion","la liste marue non reconnue")
            }
        })
    }
    /**
     * les fonctions d'initialisation
     */

    //
    private fun init(view : View ){
        marqueAdapter = MarqueAdapter(marqueList,view.context,view,marqueList,activity!!){
            Toast.makeText(view.context , it.NomVersion , Toast.LENGTH_LONG).show()
            codeVersion = it.CodeVersion
            nameVersion = it.NomVersion
        }
        initLineaire(view,R.id.imd_rv_marque,
            LinearLayoutManager.VERTICAL,marqueAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }


    //Get the fuel type used
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initFeaturesLayout(view : View, inf : LayoutInflater){

        val position = 0
        featuresDropDown = view.findViewById(R.id.other_features)
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
                txtCarb.setTextColor(context!!.resources.getColor( R.color.colorPrimary, null))
            }
        }
    }


    //Get the range of cars year edition
    fun  initYearsSpinner(view : View){
        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1980..thisYear) {
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
    private fun initPriceRange(view : View){
        val priceRange = view.findViewById<RangeSeekBar<Int>>(R.id.price_range)

        priceRange.setOnRangeSeekBarChangeListener { bar, minValue, maxValue ->
            maxPrix = maxValue * 1000000
            minPrix = minValue * 1000000
        }

    }



    //Get the maximum kilometrege
    private fun initKmRange(view : View){
        val priceRange = view.findViewById<RangeSeekBar<Int>>(R.id.Km_range)

        priceRange.setOnRangeSeekBarChangeListener { _, _, maxValue ->
            maxKm = (maxValue * 10000)
        }

    }



}


