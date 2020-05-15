package com.jetbrains.handson.mpp.taxiapp.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Trip(

    @SerializedName("startAddress")
    var startAddress: Address? = null,

    @SerializedName("endAddress")
    var endAddress: Address? = null,

    @SerializedName("price")
    var price: Price? = null,

    @SerializedName("orderTime")
    var date: Date? = null,

    @SerializedName("vehicle")
    var vehicle: Vehicle? = null
) {

    fun getShortDateAsString(): String =
        SimpleDateFormat("d MMM yyyy, 'at' HH:mm", Locale.getDefault()).format(date!!)

    fun getFullDateAsString(): String =
        SimpleDateFormat("d MMM yyyy', at' HH:mm z", Locale.getDefault()).format(date!!)

    fun getCost(): String =
        String.format("%.2f", price?.amount?.div(100)?.toDouble()) + " " + price?.currency

    fun getFullAddressStart(): String = getFullAddress(startAddress)
    fun getFullAddressEnd(): String = getFullAddress(endAddress)

    fun getShortAddressStart(): String? = startAddress?.address
    fun getShortAddressEnd(): String? = endAddress?.address

    private fun getFullAddress(address: Address?): String {
        return address?.city + ", " + address?.address
    }
}