package com.jetbrains.handson.mpp.taxiapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jetbrains.handson.mpp.taxiapp.api.RetroClient
import com.jetbrains.handson.mpp.taxiapp.model.Trip
import com.jetbrains.handson.mpp.taxiapp.view.TripAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var context: Context
    private lateinit var tripAdapter: TripAdapter
    private var api = RetroClient.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.trips_list_view)
        context = this

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                loadJson()
            }
        })
        loadJson()
    }

    private fun loadJson() {
        val call: Call<List<Trip>> = api.JSON

        call.enqueue(object : Callback<List<Trip>> {
            override fun onResponse(
                call: Call<List<Trip>>,
                response: Response<List<Trip>>
            ) {
                populateListView(response.body()!!)
            }

            override fun onFailure(
                call: Call<List<Trip>>,
                throwable: Throwable
            ) {
                println(throwable.message)
                Toast.makeText(this@MainActivity, throwable.message, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun populateListView(tripList: List<Trip>) {
        val sortedTripList = tripList.sortedBy { order -> order.date }
        tripAdapter = TripAdapter(this, sortedTripList)
        listView.setAdapter(tripAdapter)
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedOrder = sortedTripList.get(position)

            val detailIntent = selectedOrder.let {
                TripDetailsActivity.newIntent(
                    context,
                    it
                )
            }

            startActivity(detailIntent)
        }
    }
}