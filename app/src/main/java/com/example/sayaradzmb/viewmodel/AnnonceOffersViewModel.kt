package com.example.sayaradzmb.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.repository.servics.OffreService
import com.example.sayaradzmb.servics.ServiceBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AnnonceOffersViewModel : ViewModel() {
    private var usedcars: MutableLiveData<List<Offre>>? = null
    private var annonceList = ArrayList<Offre>()

    fun getUsedCars(): LiveData<List<Offre>> {
        if (usedcars == null) {
            usedcars = MutableLiveData()
        }
        return usedcars!!
    }

    fun loadUsedCars(id: Int) {

        // Here we will load the books from the Google Books API
        val service = prepareService()
        val requestCall = service.LoadOffers(id)

        requestCall.enqueue(object : Callback<List<Offre>> {
            override fun onFailure(call: Call<List<Offre>>, t: Throwable) {
                Log.e("Call response", "Can't get the data", t.cause)
            }

            override fun onResponse(call: Call<List<Offre>>, response: Response<List<Offre>>) {
                if (response.isSuccessful) {
                    for (e in response.body()!!) {
                        annonceList.add(e)
                    }
                    usedcars!!.value = annonceList
                } else {
                    Log.i("response assert", "couldn't get the data correctly")
                }
            }
        })
    }

    private fun prepareService(): OffreService {
        return ServiceBuilder.buildService(OffreService::class.java)
    }

    fun updateOffersList(id: Int, state: String, index: Int) {
        val service = prepareService()
        val requestCall = service.UpdateOfferState(id, state)

        requestCall.enqueue(object : Callback<Offre> {
            override fun onFailure(call: Call<Offre>, t: Throwable) {
                Log.e("update offre", "it didnt pass", t)
            }

            override fun onResponse(call: Call<Offre>, response: Response<Offre>) {
                if (response.isSuccessful) {
                    val el = response.body()!!
                    usedcars!!.value!![index].Etat = el.Etat
                    usedcars!!.value = usedcars!!.value
                }
            }

        })
    }

    fun deleteOffer(offer: Offre, position: Int) {
        val service = ServiceBuilder.buildService(OffreService::class.java)
        val deleteReq = service.DeleteOffer(offer.idOffre)

        deleteReq.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("delete annonce", "Something went wrong", t)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    val list = usedcars!!.value!!.toMutableList()
                    list.removeAt(position)
                    usedcars!!.value = list

                } else {
                    Log.w("delete annonce", "the req passed nut Something went wrong")

                }
            }

        })

    }

}


