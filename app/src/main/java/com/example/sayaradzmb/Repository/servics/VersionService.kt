package com.example.sayaradzmb.servics

import com.example.sayaradzmb.model.Automobiliste
import com.example.sayaradzmb.model.Version
import retrofit2.Call
import retrofit2.http.*
import java.math.BigInteger

interface VersionService {

    @GET("automobiliste/{idAutomob}/marques/modeles/{CodeModele}/versions")
    fun getVersions(@Path("idAutomob") idAutomob : BigInteger, @Path("CodeModele") idMarque : Int) : Call<List<Version>>

    @GET("marques/modeles/versions/{id}")
    fun getVersionInfo(@Path("id") id : Int) : Call<Version>

    @POST("suivies/versions/{id}")
    fun suivreVersion(@Path("id") id : Int,@Body automobiliste : Automobiliste) : Call<Any>

    @DELETE("automobiliste/{idAutomob}/modeles/{id}")
    fun desuivreVersion(@Path("id") id : Int,@Path("idAutomob") automobiliste : BigInteger) : Call<Any>




}