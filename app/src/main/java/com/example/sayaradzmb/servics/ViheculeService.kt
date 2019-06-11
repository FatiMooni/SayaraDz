package com.example.sayaradzmb.servics

import com.example.sayaradzmb.model.*
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.model.Modele
import com.example.sayaradzmb.model.version
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.math.BigInteger

interface ViheculeService {

    @GET("marques")
    fun getMarques() : Call<List<Marque>>

    @GET("automobiliste/{idAutomob}/marques/{CodeMarque}/modeles")
    fun getModeles(@Path("idAutomob") idAutomob : BigInteger,@Path("CodeMarque") idMarque : Int) : Call<List<Modele>>

    @GET("automobiliste/{idAutomob}/marques/modeles/{CodeModele}/versions")
    fun getVersions(@Path("idAutomob") idAutomob : BigInteger,@Path("CodeModele") idMarque : Int) : Call<List<version>>

    @GET("marques/modeles/versions/{id}/couleurs")
    fun getCoulours(@Path("id") id:Int) : Call<List<String>>

    @GET("marques/modeles/versions/{id}/options")
    fun getOptions(@Path("id") id : Int) : Call<List<Option>>

    @GET("marques/modeles/versions/{id}")
    fun getVersionInfo(@Path("id") id : Int) : Call<version>

    







}