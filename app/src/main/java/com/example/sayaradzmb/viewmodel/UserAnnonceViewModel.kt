package com.example.sayaradzmb.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.sayaradzmb.model.Annonce
import com.example.sayaradzmb.repository.servics.AnnonceService
import com.example.sayaradzmb.servics.ServiceBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAnnonceViewModel: ViewModel() {

    private var annonce: MutableLiveData<List<Annonce>>? = null


    fun getAnnonce(): LiveData<List<Annonce>> {
        if (annonce == null) {
            annonce = MutableLiveData()
        }
        return annonce!!
    }

    fun loadAnnounces(id: String) {

        val service = prepareService()
        val requestCall = service.GetAnnouncement(id)
        requestCall.enqueue(object : Callback<List<Annonce>> {
            override fun onFailure(call: Call<List<Annonce>>, t: Throwable) {
                Log.e("Call response", "Can't get the data", t.cause)
            }

            override fun onResponse(call: Call<List<Annonce>>, response: Response<List<Annonce>>) {
                if (response.isSuccessful) {

                    annonce!!.value = response.body()
                } else {
                    Log.i("response assert", "couldn't get the data correctly")
                }
            }
        })
    }

    fun supprimerAnnonce(id: Int) {
        val service = ServiceBuilder.buildService(AnnonceService::class.java)
        val deleteReq = service.DeleteAnnouncement(id)

        deleteReq.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("delete annonce", "Something went wrong", t)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                } else {
                    Log.w("delete annonce", "the req passed nut Something went wrong")

                }
            }

        })

    }

    private fun prepareService(): AnnonceService {
        return ServiceBuilder.buildService(AnnonceService::class.java)
    }

}