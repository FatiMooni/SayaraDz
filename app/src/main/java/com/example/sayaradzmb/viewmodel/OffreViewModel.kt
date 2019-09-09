package com.example.sayaradzmb.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.sayaradzmb.repository.servics.OffreService
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.servics.ServiceBuilder
import com.pusher.pushnotifications.PushNotifications
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OffreViewModel : ViewModel() {

    private var offre: MutableLiveData<Offre>? = null


    fun getOffre(): LiveData<Offre> {
        if (offre == null) {
            offre = MutableLiveData()
        }
        return offre!!
    }

    fun postOffre(id: String, idAnnonce : Int , Montant : Int) {

        // Here we will create service
        val service = prepareService()


        //post request
        val requestCall = service.AddOffer(idAnnonce, id, Montant)

        //evaluaton de la réponse
        requestCall.enqueue(object : Callback<Offre> {
            override fun onResponse(call: Call<Offre>, response: Response<Offre>) {
                if (response.isSuccessful) {
                    offre!!.value = response.body()
                    PushNotifications.addDeviceInterest("OFFRE+${response.body()!!.idOffre}")

                } else {
                    Log.e("Offre Adding","I got the response but not successful")
                    Log.i("Offre error ", response.message())
                }
            }

            override fun onFailure(call: Call<Offre>, t: Throwable) {
                Log.e("Offre Adding"," not successful" , t)

            }

        })
    }

    private fun prepareService(): OffreService {
        return ServiceBuilder.buildService(OffreService::class.java)
    }
}


