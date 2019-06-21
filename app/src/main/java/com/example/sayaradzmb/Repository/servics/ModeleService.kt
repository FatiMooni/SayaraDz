package com.example.sayaradzmb.servics

import com.example.sayaradzmb.model.Automobiliste
import com.example.sayaradzmb.model.Modele
import retrofit2.Call
import retrofit2.http.*
import java.math.BigInteger

interface ModeleService {

    @GET("automobiliste/{idAutomob}/marques/{CodeMarque}/modeles")
    fun getModeles(@Path("idAutomob") idAutomob : BigInteger, @Path("CodeMarque") idMarque : Int) : Call<List<Modele>>

    @POST("suivies/modeles/{id}")
    fun suivreModeles(@Path("id") id : Int,@Body automobiliste : Automobiliste) : Call<Any>

    @DELETE("automobiliste/{idAutomob}/modeles/{id}")
    fun desuivreModele(@Path("id") id : Int,@Path("idAutomob") automobiliste : BigInteger) : Call<Any>

}