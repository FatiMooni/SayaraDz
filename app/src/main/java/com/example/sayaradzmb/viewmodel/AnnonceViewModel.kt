package com.example.sayaradzmb.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.sayaradzmb.model.Annonce
import com.example.sayaradzmb.model.Commande
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.repository.servics.AnnonceService
import com.example.sayaradzmb.repository.servics.CommandesServices
import com.example.sayaradzmb.servics.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnnonceViewModel: ViewModel() {

    private var annonce: MutableLiveData<Annonce>? = null


    fun getAnnonce(): LiveData<Annonce> {
        if (annonce == null) {
            annonce = MutableLiveData()
        }
        return annonce!!
    }

    fun loadAnnounce(id: Int) {

        // Here we will load the books from the Google Books API
        val service = prepareService()
        val requestCall = service.GetAnnounceAt(id)
        requestCall.enqueue(object : Callback<Annonce> {
            override fun onFailure(call: Call<Annonce>, t: Throwable) {
                Log.e("Call response", "Can't get the data", t.cause)
            }

            override fun onResponse(call: Call<Annonce>, response: Response<Annonce>) {
                if (response.isSuccessful) {

                    annonce!!.value = response.body()
                } else {
                    Log.i("response assert", "couldn't get the data correctly")
                }
            }
        })
    }

    private fun prepareService(): AnnonceService {
        return ServiceBuilder.buildService(AnnonceService::class.java)
    }

}