package com.jetbrains.handson.mpp.taxiapp.model

import com.google.gson.annotations.SerializedName

data class Vehicle(
    @SerializedName("regNumber")
    var registrationPlate: String? = null,

    @SerializedName("modelName")
    var modelName: String? = null,

    @SerializedName("photo")
    var photoName: String? = null,

    @SerializedName("driverName")
    var driverName: String? = null
)