package com.example.sayaradzmb.repository.servics

import com.example.sayaradzmb.model.Commande
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CommandesServices {
    @GET("annonces/{id}")
    fun GetMyCommand(
        @Path("id") id :String
    ) : Call<List<Commande>>
}