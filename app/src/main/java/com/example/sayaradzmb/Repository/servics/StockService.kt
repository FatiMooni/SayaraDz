package com.example.sayaradzmb.Repository.servics


import com.example.sayaradzmb.model.VoitureCommande
import retrofit2.Call

import retrofit2.http.GET

import retrofit2.http.Path

interface StockService {
    /**
     * Avoir les different vihecule en stock
     */
    @GET("vehicules/stock/{id}")
    fun avoirVoitureStock(@Path("id") id : Int) : Call<List<VoitureCommande>>
}