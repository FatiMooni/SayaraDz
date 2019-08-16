package com.example.sayaradzmb.repository.servics

import com.example.sayaradzmb.model.Offre
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

interface OffreService {

    @FormUrlEncoded
    @POST("vehicules/annonces/{idAnnonce}/offres")
             fun AddOffer(@Path("idAnnonce") idAnnonce : Int,
                          @Field("idAutomobiliste") idAutomobiliste : String,
                          @Field("Montant") Montant : Int
                          ) : Call<Offre>

    @GET("vehicules/annonces/{idAnnonce}/offres")
            fun LoadOffers(@Path("idAnnonce") idAnnonce : Int): Call<List<Offre>>

    @GET ( "automobiliste/{idAutomobiliste}/offres")
            fun LoadUserOffers(@Path("idAutomobiliste")idAutomobiliste : String
            ): Call<List<Offre>>

    @PUT ("vehicules/annonces/offres/{idOffre}/{state}")
            fun UpdateOfferState(@Path("idOffre") idOffre: Int,
                                 @Path("state") state: String) : Call<Offre>

    @DELETE ("vehicules/annonces/offres/{idOffre}")
            fun  DeleteOffer(@Path("idOffre") idOffre: Int): Call<ResponseBody>
}