package com.example.sayaradzmb.Repository.servics

import com.example.sayaradzmb.model.Offre
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface OffreService {

    @FormUrlEncoded
    @POST("vehicules/annonces/{idAnnonce}/offres")
             fun AddOffer(@Path("idAnnonce") idAnnonce : Int,
                          @Field("idAutomobiliste") idAutomobiliste : String,
                          @Field("Montant") Montant : Int
                          ) : Call<Offre>
}