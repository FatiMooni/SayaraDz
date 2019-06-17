package com.example.sayaradzmb.servics

import com.example.sayaradzmb.model.*
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.model.Modele
import com.example.sayaradzmb.model.version
import retrofit2.Call
import retrofit2.http.*
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

    @POST("suivies/modeles/{id}")
    fun suivreModeles(@Path("id") id : Int,@Body automobiliste : Automobiliste) : Call<Any>

    @POST("suivies/versions/{id}")
    fun suivreVersion(@Path("id") id : Int,@Body automobiliste : Automobiliste) : Call<Any>

    @DELETE("automobiliste/{idAutomob}/modeles/{id}")
    fun desuivreModele(@Path("id") id : Int,@Path("idAutomob") automobiliste : BigInteger) : Call<Any>

    @DELETE("automobiliste/{idAutomob}/modeles/{id}")
    fun desuivreVersion(@Path("id") id : Int,@Path("idAutomob") automobiliste : BigInteger) : Call<Any>







}