package com.jetbrains.handson.mpp.taxiapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.concurrent.ConcurrentHashMap


class TimedDiskBitmapCache(context: Context, expiryTimeInMillis: Long) {
    private var context: Context
    private val mExpiryTimeInMillis: Long
    private val mTimeMap: MutableMap<String, Long>
    private val storageMap: HashMap<String, String>
    private val cacheDir: File

    private fun isValidKey(key: String?): Boolean {
        return key != null && mTimeMap.containsKey(key)
    }

    @Synchronized
    operator fun get(key: String): Bitmap? {
        return getIfNotExpired(key, System.currentTimeMillis() - mExpiryTimeInMillis)
    }

    @Synchronized
    private fun getIfNotExpired(key: String, expiryTimeInMillis: Long): Bitmap? {
        if (!isValidKey(key)) {
            return null
        }
        return if (mTimeMap[key]!! >= expiryTimeInMillis) {
            getImage(key)
        } else {
            remove(key)
            null
        }
    }

    @Synchronized
    fun put(key: String?, value: Bitmap?) {
        if (key != null && value != null) {
            saveImage(key, value)
            mTimeMap[key] = System.currentTimeMillis()
        }
    }

    private fun getImage(fileName: String): Bitmap {
        val fileAbsolutePath = storageMap.get(fileName)
        val bitmap = BitmapFactory.decodeFile(fileAbsolutePath)
        return bitmap
    }

    private fun saveImage(fileName: String, bitmap: Bitmap) {

        val file = File(cacheDir, fileName)
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG)
                .show()
        }
        storageMap.put(fileName, file.absolutePath)
    }

    @Synchronized
    fun remove(key: String?) {
        if (key != null) {
            mTimeMap.remove(key)
            val absolutePath = storageMap.get(key)
            File(absolutePath!!).delete()
        }
    }

    @Synchronized
    fun clear() {
        storageMap.keys.forEach { key -> remove(key) }
        storageMap.clear()
        mTimeMap.clear()
    }

    companion object {
        private const val DISK_CACHE_SUBDIR = "thumbnails"
    }

    init {
        this.context = context
        storageMap = HashMap()
        mTimeMap = ConcurrentHashMap()
        mExpiryTimeInMillis = expiryTimeInMillis

        cacheDir = File(context.cacheDir.path + File.separator + DISK_CACHE_SUBDIR)
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }
}