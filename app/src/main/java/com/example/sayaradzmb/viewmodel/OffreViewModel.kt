package com.example.sayaradzmb.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.widget.Toast
import com.example.sayaradzmb.Repository.servics.OffreService
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.servics.ServiceBuilder
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

        //creation d'une instance sur le service des annonces


        //les differentes parts du post request
        val code = RequestBody.create(MediaType.parse("text/plain"), idAnnonce.toString())
        val idAu = RequestBody.create(MediaType.parse("text/plain"), id)
        val price= RequestBody.create(MediaType.parse("text/plain"), Montant.toString())


        //post request
        val requestCall = service.AddOffer(idAnnonce, id, Montant)

        //evaluaton de la réponse
        requestCall.enqueue(object : Callback<Offre> {
            override fun onResponse(call: Call<Offre>, response: Response<Offre>) {
                if (response.isSuccessful) {
                    offre!!.value = response.body()
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


