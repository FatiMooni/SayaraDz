package com.example.sayaradzmb.Repository.servics

import com.example.sayaradzmb.model.VoitureCommande
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface CommandeService {

    /**
     * cree une commande
     */
    @FormUrlEncoded
    @POST("vehicules/commandes")
    fun creeCommande(@Field("Montant") montant : String,
                          @Field("idAutomobiliste") idAutomb : String,
                          @Field("NumChassis") chassis : String,
                          @Field("Fabricant") fabricant : String) : Call<Any>
}