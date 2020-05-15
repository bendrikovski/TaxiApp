package com.jetbrains.handson.mpp.taxiapp.utils

import android.graphics.Bitmap
import android.util.LruCache

class BitmapCache(maxSize: Int) : LruCache<String?, Bitmap?>(maxSize) {
    protected fun sizeOf(key: String?, value: Bitmap): Int {
        return value.byteCount / 1024
    }

    fun getBitmap(key: String?): Bitmap? {
        return this[key]
    }

    fun setBitmap(key: String?, bitmap: Bitmap?) {
        if (!hasBitmap(key)) {
            this.put(key, bitmap)
        }
    }

    fun hasBitmap(key: String?): Boolean {
        return getBitmap(key) != null
    }

    companion object {
        val cacheSize: Int
            get() {
                val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
                return maxMemory / 8
            }
    }
}