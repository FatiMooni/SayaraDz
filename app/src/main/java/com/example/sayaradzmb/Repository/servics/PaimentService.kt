package com.example.sayaradzmb.Repository.servics

import com.example.sayaradzmb.model.PaimentToken
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface PaimentService {

    /**
     * avoir le token
     */
    @GET("vehicules/reservations/demande")
    fun getBraintreeToken() : Call<PaimentToken>

    /**
     * le paiment
     */
    @FormUrlEncoded
    @POST("vehicules/reservations/paiement")
    fun postNonceToServer(@Field("payment_method_nonce") paiment : String,
                          @Field("Montant") montant : String,
                          @Field("idCommande") commande : String) : Call<Any>
}