package com.jetbrains.handson.mpp.taxiapp.model

import com.google.gson.annotations.SerializedName


data class Address(
    @SerializedName("city")
    var city: String? = null,

    @SerializedName("address")
    var address: String? = null
)