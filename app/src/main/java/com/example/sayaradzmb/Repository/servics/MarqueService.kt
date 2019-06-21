package com.example.sayaradzmb.servics

import com.example.sayaradzmb.model.Marque
import retrofit2.Call
import retrofit2.http.GET

interface MarqueService {

    @GET("marques")
    fun getMarques() : Call<List<Marque>>
}