package com.example.sayaradzmb.ui.activities.fragments


import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.sayaradzmb.R
import com.example.sayaradzmb.customs.CustomFiltersInitializer
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.model.VehiculeRechFilters
import com.example.sayaradzmb.servics.MarqueService
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.ui.adapter.MarqueAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("UNCHECKED_CAST")
class OccasionRechFragment : Fragment(), RecycleViewHelper, CustomFiltersInitializer {

    override var itemRecycleView: RecyclerView? = null
    private var marqueList = ArrayList<Marque>()
    private var marqueAdapter: MarqueAdapter? = null

    private lateinit var item: VehiculeRechFilters

    //variables utilisés dans la requete
    private var nameVersion: String? = null
    private var codeVersion: Int? = null
    override var typeCarburant: String = ""
    override var maxYear: Int = 0
    override var minYear: Int = 0
    override var maxPrix: Int = 0
    override var minPrix: Int = 0
    override var maxKm: Int = 0


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myView = inflater.inflate(R.layout.fragment_occasion_rech, container, false)

        //prepare the recycle view + marque list to get the code version
        init(myView)
        getMarque()


        //prepare other views to get other filters
        initFeaturesLayout(myView, inflater)
        initYearsSpinner(myView)
        initPriceRange(myView)
        initKmRange(myView)

        val btnSearch = myView.findViewById<Button>(R.id.search_button)

        //Send filters to the next fragment
        val bundle = Bundle()
        btnSearch.setOnClickListener {
            item = VehiculeRechFilters(codeVersion, typeCarburant, minPrix, maxPrix, minYear, maxYear, maxKm)
            bundle.putParcelable("filters", item)
            bundle.putCharSequence("version", nameVersion)
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
    private fun getMarque() {
        val vService = ServiceBuilder.buildService(MarqueService::class.java)
        val requeteAppel = vService.getMarques()
        requeteAppel.enqueue(object : Callback<List<Marque>> {
            override fun onResponse(call: Call<List<Marque>>, response: Response<List<Marque>>) =
                if (response.isSuccessful) {
                    print(response.body()!!)
                    val lesMarque = response.body()!!
                    lesMarque.forEach { e ->
                        marqueList.add(e)
                    }
                } else {

                }

            override fun onFailure(call: Call<List<Marque>>, t: Throwable) {
                Log.w("failConnexion", "la liste marue non reconnue")
            }
        })
    }


    /**
     * les fonctions d'initialisation
     */

    //
    private fun init(view: View) {
        marqueAdapter = MarqueAdapter(marqueList, view.context, view, marqueList, activity!!) {
            codeVersion = it.CodeVersion
            nameVersion = it.NomVersion
        }
        initLineaire(
            view, R.id.imd_rv_marque,
            LinearLayoutManager.VERTICAL, marqueAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
        )
    }

}


