package com.example.sayaradzmb.ui.activities.fragments

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.sayaradzmb.R
import com.example.sayaradzmb.ui.adapter.OccasionCarsAdapter
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.model.VehiculeRechFilters
import com.example.sayaradzmb.Repository.servics.OccasionService
import com.example.sayaradzmb.servics.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class CustomOccasionFragment : Fragment(), RecycleViewHelper {

    //pour utiliser Linear Helper
    override var itemRecycleView: RecyclerView? = null
    private var annonceList = ArrayList<VehiculeOccasion>()
    private var annonceAdapter : OccasionCarsAdapter? = null


    @Suppress("UNCHECKED_CAST")
    fun prepareRecyclerView(v : View){
        val layout = LinearLayoutManager(v.context)
        layout.orientation = LinearLayoutManager.VERTICAL
        val adapter = v.findViewById<RecyclerView>(R.id.recyler_view_voiture_occasion)
        adapter.layoutManager = layout
        annonceAdapter = OccasionCarsAdapter(v.context,annonceList )
        adapter.adapter = annonceAdapter
    }

    fun getAnnonceList(id: String, filters: VehiculeRechFilters?){
        val service = prepareService()
        val requestCall = service.GetOccasionAnnouncement(id,
            filters!!.minPrix, filters.maxPrix, filters.maxAnnee,
            filters.minAnnee, filters.maxKm, filters.codeVersion)

        requestCall.enqueue(object : Callback<List<VehiculeOccasion>> {
            override fun onFailure(call: Call<List<VehiculeOccasion>>, t: Throwable) {
                Log.e("Call response" , "Can't get the data" , t.cause)
            }

            override fun onResponse(call: Call<List<VehiculeOccasion>>, response: Response<List<VehiculeOccasion>>) {
                if(response.isSuccessful){
                    annonceList.clear()
                    annonceAdapter!!.notifyDataSetChanged()
                    for (e in response.body()!!){
                        annonceList.add(e)
                    }
                    annonceAdapter!!.notifyDataSetChanged()
                }
                else {
                    Log.i("response assert" , "couldn't get the data correctly")
                }
            }

        })
    }

    private fun prepareService(): OccasionService {
        return ServiceBuilder.buildService(OccasionService::class.java)
    }
}