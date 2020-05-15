package com.jetbrains.handson.mpp.taxiapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jetbrains.handson.mpp.taxiapp.api.ImageLoadCallback
import com.jetbrains.handson.mpp.taxiapp.api.RetroClient
import com.jetbrains.handson.mpp.taxiapp.model.Trip
import com.jetbrains.handson.mpp.taxiapp.utils.TimedDiskBitmapCache
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TripDetailsActivity : AppCompatActivity() {
    private var api = RetroClient.apiService
    private lateinit var context: Context
    private lateinit var timedDiskBitmapCache: TimedDiskBitmapCache

    companion object {
        const val COST = "cost"
        const val FULL_DATE = "full_date"
        const val FULL_ADDRESS_START = "full_address_start"
        const val FULL_ADDRESS_END = "full_address_end"
        const val DRIVER_NAME = "driver_name"
        const val CAR_MODEL = "car_model"
        const val VEHICLE_REG_PLATE = "vehicle_registration_plate"
        const val VEHICLE_PHOTO_NAME = "photoName"

        fun newIntent(context: Context, trip: Trip): Intent {
            val detailIntent = Intent(context, TripDetailsActivity::class.java)
            detailIntent.putExtra(COST, trip.getCost())
            detailIntent.putExtra(FULL_DATE, trip.getFullDateAsString())
            detailIntent.putExtra(FULL_ADDRESS_START, trip.getFullAddressStart())
            detailIntent.putExtra(FULL_ADDRESS_END, trip.getFullAddressEnd())
            detailIntent.putExtra(DRIVER_NAME, trip.vehicle?.driverName)
            detailIntent.putExtra(CAR_MODEL, trip.vehicle?.modelName)
            detailIntent.putExtra(VEHICLE_REG_PLATE, trip.vehicle?.registrationPlate)
            detailIntent.putExtra(VEHICLE_PHOTO_NAME, trip.vehicle?.photoName)

            return detailIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)
        context = this
        val app = this.application as MyApplication
        timedDiskBitmapCache = app.getDiskCache()!!

        val driver_name = findViewById<TextView>(R.id.driver_name)
        val car_model = findViewById<TextView>(R.id.car_model)
        val vehicle_registration_plate = findViewById<TextView>(R.id.vehicle_registration_plate)
        val cost = findViewById<TextView>(R.id.cost)
        val date = findViewById<TextView>(R.id.date)
        val start_address = findViewById<TextView>(R.id.start_address)
        val end_address = findViewById<TextView>(R.id.end_address)
        val carImage = findViewById<ImageView>(R.id.icon)

        driver_name.setText(intent.extras?.getString(DRIVER_NAME))
        car_model.setText(intent.extras?.getString(CAR_MODEL))
        vehicle_registration_plate.setText(intent.extras?.getString(VEHICLE_REG_PLATE))
        cost.setText(intent.extras?.getString(COST))
        date.setText(intent.extras?.getString(FULL_DATE))
        start_address.setText(intent.extras?.getString(FULL_ADDRESS_START))
        end_address.setText(intent.extras?.getString(FULL_ADDRESS_END))

        val photoName = intent.extras?.getString(VEHICLE_PHOTO_NAME)

        val bitmapFromMemory = getBitmapFromMemCache(photoName!!)
        if (bitmapFromMemory != null) {
            carImage.setImageBitmap(bitmapFromMemory)
        } else {
            loadImage(photoName, object : ImageLoadCallback {
                override fun onSuccess(bitmap: Bitmap) {
                    carImage.setImageBitmap(bitmap)
                    addBitmapToMemoryCache(photoName, bitmap)
                }

                override fun onError(throwable: Throwable?) {
                    carImage.setImageResource(R.drawable.ic_launcher_background)
                }
            })
        }
    }

    private fun addBitmapToMemoryCache(key: String, bitmap: Bitmap?) {
        if (getBitmapFromMemCache(key) == null) {
            timedDiskBitmapCache.put(key, bitmap)
        }
    }

    private fun getBitmapFromMemCache(key: String): Bitmap? {
        return timedDiskBitmapCache.get(key)
    }

    private fun loadImage(
        photoName: String,
        callback: ImageLoadCallback
    ) {
        val call = api.uploadImage(photoName)
        var bitmap: Bitmap
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
                callback.onSuccess(bitmap)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable?) {
                callback.onError(t)
            }
        })
    }
}
