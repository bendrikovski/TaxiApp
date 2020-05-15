package com.jetbrains.handson.mpp.taxiapp.api

import com.jetbrains.handson.mpp.taxiapp.model.Trip
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {
    @get:GET("/careers/test/orders.json")
    val JSON: Call<List<Trip>>

    @GET("/careers/test/images/{imageName}")
    fun uploadImage(
        @Path("imageName") imageName: String
    ): Call<ResponseBody>
}