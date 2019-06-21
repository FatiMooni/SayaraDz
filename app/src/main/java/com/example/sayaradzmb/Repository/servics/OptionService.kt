package com.example.sayaradzmb.servics

import com.example.sayaradzmb.model.Option
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OptionService {

    @GET("marques/modeles/versions/{id}/options")
    fun getOptions(@Path("id") id : Int) : Call<List<Option>>
}