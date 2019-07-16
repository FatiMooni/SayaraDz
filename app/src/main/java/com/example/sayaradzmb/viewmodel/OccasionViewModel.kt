package com.example.sayaradzmb.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.view.View
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.model.VehiculeRechFilters
import com.example.sayaradzmb.repository.servics.OccasionService
import com.example.sayaradzmb.servics.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OccasionViewModel : ViewModel() {
    private var usedcars: MutableLiveData<List<VehiculeOccasion>>? = null
    private var annonceList = ArrayList<VehiculeOccasion>()

    fun getUsedCars(): LiveData<List<VehiculeOccasion>> {
        if (usedcars == null) {
            usedcars = MutableLiveData()
        }
        return usedcars!!
    }

    fun loadUsedCars(id: String, filters: VehiculeRechFilters?) {

        // Here we will load the books from the Google Books API
        val service = prepareService()
        val requestCall = service.GetOccasionAnnouncement(id,
            filters!!.minPrix, filters.maxPrix, filters.minAnnee,
            filters.maxAnnee, filters.maxKm, filters.codeVersion)

        requestCall.enqueue(object : Callback<List<VehiculeOccasion>> {
            override fun onFailure(call: Call<List<VehiculeOccasion>>, t: Throwable) {
                Log.e("Call response" , "Can't get the data" , t.cause)
            }

            override fun onResponse(call: Call<List<VehiculeOccasion>>, response: Response<List<VehiculeOccasion>>) {
                if(response.isSuccessful){
                    for (e in response.body()!!){
                        annonceList!!.add(e)
                    }
                    usedcars!!.value = annonceList
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


