package com.example.sayaradzmb.servics

import com.example.sayaradzmb.model.*
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.model.Modele
import com.example.sayaradzmb.model.version
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ViheculeService {

    @GET("marques")
    fun getMarques() : Call<List<Marque>>

    @GET("marques/{id}/modeles")
    fun getModeles(@Path("id") id : Int) : Call<List<Modele>>

    @GET("marques/modeles/{id}/versions")
    fun getVersions(@Path("id") id :Int) : Call<List<version>>

    @GET("marques/modeles/versions/{id}/couleurs")
    fun getCoulours(@Path("id") id:Int) : Call<List<String>>

    @GET("marques/modeles/versions/{id}/options")
    fun getOptions(@Path("id") id : Int) : Call<List<Option>>

    @GET("marques/modeles/versions/{id}")
    fun getVersionInfo(@Path("id") id : Int) : Call<version>


}