package com.jetbrains.handson.mpp.taxiapp.api

import android.graphics.Bitmap


interface ImageLoadCallback {
    fun onSuccess(bitmap: Bitmap)
    fun onError(throwable: Throwable?)
}