package com.jetbrains.handson.mpp.taxiapp.model

import com.google.gson.annotations.SerializedName

data class Price(
    @SerializedName("amount")
    var amount: Long? = null,

    @SerializedName("currency")
    var currency: String? = null
)