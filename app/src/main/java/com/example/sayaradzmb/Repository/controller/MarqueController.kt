package com.example.sayaradzmb.Repository.controller

import android.util.Log
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.servics.MarqueService
import com.example.sayaradzmb.servics.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MarqueController {

    var marqueService = ServiceBuilder.buildService(MarqueService::class.java)

    fun getAllMarque() : ArrayList<Marque>{
        var marqueList : ArrayList<Marque>? = null
        val requeteAppel = marqueService.getMarques()
        requeteAppel.enqueue(object : Callback<List<Marque>> {
            override fun onResponse(call: Call<List<Marque>>, response: Response<List<Marque>>) =
                if(response.isSuccessful){
                    print(response.body()!!)
                    var lesMarque = response.body()!!
                    lesMarque.forEach{
                            e->marqueList!!.add(e)
                    }
                    //marqueAdapter!!.addAllwithclear(marqueList)
                }else{

                }
            override fun onFailure(call: Call<List<Marque>>, t: Throwable) {
                Log.w("failConnexion","la liste marue non reconnue")
            }
        })
        return marqueList!!
    }

}