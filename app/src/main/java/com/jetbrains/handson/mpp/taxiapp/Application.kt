package com.jetbrains.handson.mpp.taxiapp

import android.app.Application
import com.jetbrains.handson.mpp.taxiapp.utils.BitmapCache
import com.jetbrains.handson.mpp.taxiapp.utils.TimedDiskBitmapCache

class MyApplication : Application() {
    private lateinit var memoryCache: BitmapCache
    private lateinit var timedDiskBitmapCache: TimedDiskBitmapCache

    override fun onCreate() {
        super.onCreate()
        memoryCache = BitmapCache(BitmapCache.cacheSize)
        timedDiskBitmapCache =
            TimedDiskBitmapCache(
                this.applicationContext,
                1 * 60 * 1000
            )
    }

    fun getMemoryCache(): BitmapCache? {
        return memoryCache
    }

    fun getDiskCache(): TimedDiskBitmapCache? {
        return timedDiskBitmapCache
    }

}