package com.jetbrains.handson.mpp.taxiapp.model

import com.google.gson.annotations.Expose

class TripList {
    @Expose
    private var trips: ArrayList<Trip> = ArrayList()

    fun getTrips(): ArrayList<Trip> {
        return trips
    }

}