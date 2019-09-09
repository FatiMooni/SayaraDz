package com.example.sayaradzmb.servics

import com.example.sayaradzmb.model.Automobiliste
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("api/auth")
    fun setToken(@Header("Authorization") authorization:String, @Body idAutomobilste: Automobiliste) : Call<Automobiliste>
}