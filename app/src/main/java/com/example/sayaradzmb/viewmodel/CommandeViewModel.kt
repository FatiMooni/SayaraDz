package com.example.sayaradzmb.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Commande
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.model.VehiculeRechFilters
import com.example.sayaradzmb.repository.servics.CommandesServices
import com.example.sayaradzmb.servics.OccasionService
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.ui.adapter.OccasionCarsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommandeViewModel :ViewModel(){
    private var commandes: MutableLiveData<List<Commande>>? = null
    private var carsList = ArrayList<Commande>()

    fun getCommande(): LiveData<List<Commande>> {
        if (commandes == null) {
            commandes = MutableLiveData()
        }
        return commandes!!
    }

    fun loadMesCommandes(id : String) {

        // Here we will load the books from the Google Books API
        val service = prepareService()
        val requestCall = service.GetMyCommand(id)
        requestCall.enqueue(object : Callback<List<Commande>> {
            override fun onFailure(call: Call<List<Commande>>, t: Throwable) {
                Log.e("Call response" , "Can't get the data" , t.cause)
            }

            override fun onResponse(call: Call<List<Commande>>, response: Response<List<Commande>>) {
                if(response.isSuccessful){
                    for (e in response.body()!!){
                        carsList.add(e)
                    }
                    commandes!!.value = carsList
                }
                else {
                    Log.i("response assert" , "couldn't get the data correctly")
                }
            }
        })
    }
    private fun prepareService(): CommandesServices {
        return ServiceBuilder.buildService(CommandesServices::class.java)
    }

}