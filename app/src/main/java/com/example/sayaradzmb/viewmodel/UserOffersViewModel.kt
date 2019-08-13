package com.example.sayaradzmb.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.repository.servics.OffreService
import com.example.sayaradzmb.servics.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserOffersViewModel : ViewModel() {

    private var offreList: MutableLiveData<List<Offre>>? = null


    fun getOffre(): LiveData<List<Offre>> {
        if (offreList == null) {
            offreList = MutableLiveData()
        }
        return offreList!!
    }

    fun loadOffers(id: String) {// Here we will load the books from the Google Books API
        val service = prepareService()
        val requestCall = service.LoadUserOffers(id)
        requestCall.enqueue(object : Callback<List<Offre>> {
            override fun onFailure(call: Call<List<Offre>>, t: Throwable) {
                Log.e("Call response" , "Can't get the data" , t.cause)
            }

            override fun onResponse(call: Call<List<Offre>>, response: Response<List<Offre>>) {
                if(response.isSuccessful){

                    offreList!!.value = response.body()!!
                }
                else {
                    Log.i("response assert" , "couldn't get the data correctly")
                }
            }
        })
    }
    private fun prepareService(): OffreService {
        return ServiceBuilder.buildService(OffreService::class.java)
    }

}