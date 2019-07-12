package com.example.sayaradzmb.Repository.servics


import com.example.sayaradzmb.model.VehiculeOccasion
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OccasionService {
    @GET("annonces/{id}")
    fun GetOccasionAnnouncement(
        @Path("id") id :String,
        @Query("minPrix") minPrix : Int?,
        @Query("maxPrix") maxPrix : Int?,
        @Query("minAnnee") minAnnee : Int?,
        @Query("maxAnnee") maxAnnee : Int?,
        @Query("maxKm") maxKm : Int?,
        @Query("CodeVersion") CodeVersion : Int?
    ) : Call<List<VehiculeOccasion>>

}