package com.jetbrains.handson.mpp.taxiapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetroClient {
    private const val ROOT_URL: String = "https://www.roxiemobile.ru"

    private val retrofitInstance: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val apiService: ApiService
        get() {
            return retrofitInstance.create(ApiService::class.java)
        }
}

