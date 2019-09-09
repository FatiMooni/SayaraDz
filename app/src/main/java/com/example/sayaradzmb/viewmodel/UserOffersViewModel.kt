package com.example.sayaradzmb.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.model.UserOffre
import com.example.sayaradzmb.repository.servics.OffreService
import com.example.sayaradzmb.servics.ServiceBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserOffersViewModel : ViewModel() {

    private var offreList: MutableLiveData<List<UserOffre>>? = null


    fun getOffre(): LiveData<List<UserOffre>> {
        if (offreList == null) {
            offreList = MutableLiveData()
        }
        return offreList!!
    }

    fun loadOffers(id: String) {// Here we will load the books from the Google Books API
        val service = prepareService()
        val requestCall = service.LoadUserOffers(id)
        requestCall.enqueue(object : Callback<List<UserOffre>> {
            override fun onFailure(call: Call<List<UserOffre>>, t: Throwable) {
                Log.e("Call response" , "Can't get the data" , t.cause)
            }

            override fun onResponse(call: Call<List<UserOffre>>, response: Response<List<UserOffre>>) {
                if(response.isSuccessful){

                    offreList!!.value = response.body()!!
                }
                else {
                    Log.i("response assert" , "couldn't get the data correctly")
                }
            }
        })
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
                    offreList!!.value!![index].Etat = el.Etat
                    offreList!!.value = offreList!!.value
                }
            }

        })
    }
    fun deleteOffer(position: Int) {
        val service = ServiceBuilder.buildService(OffreService::class.java)
        val deleteReq = service.DeleteOffer(position)

        deleteReq.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("delete annonce", "Something went wrong", t)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    val list = offreList!!.value!!.toMutableList()
                    list.removeAt(getPosition(position))
                    offreList!!.value = list

                } else {
                    Log.w("delete annonce", "the req passed nut Something went wrong")

                }
            }

        })

    }

    fun getPosition(id : Int) : Int{
        val list = offreList!!.value
        var index = -1
        list!!.forEach {
            if (it.idOffre == id) {
                index = list.indexOf(it)
            }
        }
        return index
    }
    fun deleteOffer(idOffer: Int, position: Int) {
        val service = ServiceBuilder.buildService(OffreService::class.java)
        val deleteReq = service.DeleteOffer(idOffer)

        deleteReq.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("delete annonce", "Something went wrong", t)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    val list = offreList!!.value!!.toMutableList()
                    list.removeAt(position)
                    offreList!!.value = list

                } else {
                    Log.w("delete annonce", "the req passed nut Something went wrong")

                }
            }

        })

    }
    private fun prepareService(): OffreService {
        return ServiceBuilder.buildService(OffreService::class.java)
    }

}