package com.jetbrains.handson.mpp.taxiapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.jetbrains.handson.mpp.taxiapp.R
import com.jetbrains.handson.mpp.taxiapp.model.Trip

class TripAdapter(
    private val context: Context,
    private val dataSource: List<Trip>
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {

            view = inflater.inflate(R.layout.trip_card, parent, false)

            holder = ViewHolder()
            holder.costTextView = view.findViewById(R.id.cost) as TextView
            holder.dateTextView = view.findViewById(R.id.date) as TextView
            holder.startAddressTextView = view.findViewById(R.id.start_address) as TextView
            holder.endAddressTextView = view.findViewById(R.id.end_address) as TextView

            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val order = getItem(position) as Trip

        holder.costTextView.text = order.getCost()
        holder.dateTextView.text = order.getShortDateAsString()
        holder.startAddressTextView.text = order.getShortAddressStart()
        holder.endAddressTextView.text = order.getShortAddressEnd()

        return view
    }

    private class ViewHolder {
        lateinit var costTextView: TextView
        lateinit var dateTextView: TextView
        lateinit var startAddressTextView: TextView
        lateinit var endAddressTextView: TextView
    }


    override fun getItem(position: Int): Any {
        return dataSource.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }
}