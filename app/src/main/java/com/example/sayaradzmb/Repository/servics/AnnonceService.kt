package com.example.sayaradzmb.servics

import com.example.sayaradzmb.model.Annonce
import com.example.sayaradzmb.model.Offre
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface AnnonceService {

     @Multipart
     @POST("vehicules/annonces/")
     fun CreateAnnouncemet(@Part("idAutomobiliste") idAutomobiliste : RequestBody,
                           @Part("CodeVersion") CodeVersion : RequestBody,
                           @Part("Prix") Prix : RequestBody,
                           @Part("Description") Description : RequestBody,
                           @Part("Couleur") Couleur : RequestBody,
                           @Part ("Km") Km :  RequestBody,
                           @Part ("Carburant") Carburant : RequestBody,
                           @Part imageAnnonce : List<MultipartBody.Part>) : Call<Annonce>

     @GET("automobiliste/{id}/annonces")
     fun GetAnnouncement(@Path("id") id :String) : Call<List<Annonce>>

     @DELETE("vehicules/annonces/{id}")
     fun DeleteAnnouncement(@Path("id") id :Int) : Call<ResponseBody>
}