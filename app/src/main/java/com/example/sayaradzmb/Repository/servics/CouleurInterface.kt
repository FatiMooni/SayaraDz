package com.example.sayaradzmb.servics

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CouleurInterface {

    @GET("marques/modeles/versions/{id}/couleurs")
    fun getCoulours(@Path("id") id:Int) : Call<List<String>>
}