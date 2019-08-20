package com.example.sayaradzmb.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.repository.servics.OccasionService
import com.example.sayaradzmb.servics.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AcceuilleViewModel : ViewModel() {
    private var usedcars: MutableLiveData<List<VehiculeOccasion>>? = null

    fun getUsedCars(): LiveData<List<VehiculeOccasion>> {
        if (usedcars == null) {
            usedcars = MutableLiveData()
        }
        return usedcars!!
    }


    fun loadUsedCars(id: String) {

        // Here we will load the books from the Google Books API
        val service =  ServiceBuilder.buildService(OccasionService::class.java)

        val requestCall = service.GetLatestAnnouncement(id)

        requestCall.enqueue(object : Callback<List<VehiculeOccasion>> {
            override fun onFailure(call: Call<List<VehiculeOccasion>>, t: Throwable) {
                Log.e("Call response" , "Can't get the data" , t.cause)
            }

            override fun onResponse(call: Call<List<VehiculeOccasion>>, response: Response<List<VehiculeOccasion>>) {
                if(response.isSuccessful){

                    usedcars!!.value = response.body()
                }
                else {
                    Log.i("response assert" , "couldn't get the data correctly")
                }
            }
        })
    }

}