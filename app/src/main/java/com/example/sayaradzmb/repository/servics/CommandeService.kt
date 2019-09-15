package com.example.sayaradzmb.repository.servics

import com.example.sayaradzmb.model.Commande
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CommandeService {

    /**
     * cree une commande
     */
    @FormUrlEncoded
    @POST("vehicules/commandes")
    fun creeCommande(@Field("Montant") montant : String,
                          @Field("idAutomobiliste") idAutomb : String,
                          @Field("NumChassis") chassis : String,
                          @Field("Fabricant") fabricant : String) : Call<Commande>
}