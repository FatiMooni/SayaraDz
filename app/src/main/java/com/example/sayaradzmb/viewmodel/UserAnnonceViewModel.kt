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

    fun supprimerAnnonce(id: Int , index : Int) {
        val service = ServiceBuilder.buildService(AnnonceService::class.java)
        val deleteReq = service.DeleteAnnouncement(id)

        deleteReq.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("delete annonce", "Something went wrong", t)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    val list = annonce!!.value!!.toMutableList()
                    list.removeAt(getPosition(id))
                    annonce!!.value = list

                } else {
                    Log.w("delete annonce", "the req passed nut Something went wrong")

                }
            }

        })

    }
    fun getPosition(id : Int) : Int{
        val list = annonce!!.value
        var index = -1
        list!!.forEach {
            if (it.idAnnonce == id) {
                index = list.indexOf(it)
            }
        }
        return index
    }

    fun UpdateAnnoncePrice(id : Int , price : String){
        val service = ServiceBuilder.buildService(AnnonceService::class.java)
        val updateReq = service.UpdateAnnounce(id,price)

        updateReq.enqueue(object : Callback<Annonce> {
            override fun onFailure(call: Call<Annonce>, t: Throwable) {
                Log.e("update annonce", "the req didnt pass nut Something went wrong")

            }

            override fun onResponse(call: Call<Annonce>, response: Response<Annonce>) {
                if (response.isSuccessful) {

                    val list = annonce!!.value!!.toMutableList()
                     val index = getPosition(id)
                    val item = list[index]
                    item.Prix = response.body()!!.Prix
                    list.set(index, item)
                    annonce!!.value = list

                } else {
                    Log.w("update annonce", "the req passed nut Something went wrong")

                }
            }

        })


    }

    private fun prepareService(): AnnonceService {
        return ServiceBuilder.buildService(AnnonceService::class.java)
    }

}