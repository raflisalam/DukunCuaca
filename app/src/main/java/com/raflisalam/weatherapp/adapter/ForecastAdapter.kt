package com.raflisalam.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raflisalam.weatherapp.R
import com.raflisalam.weatherapp.model.Condition
import com.raflisalam.weatherapp.model.Hour
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter: RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    private lateinit var listForecast: List<Hour>
    private lateinit var listCondition: List<Condition>

    fun setListForecast(listForecast: List<Hour>) {
        this.listForecast = listForecast
    }
//
//    fun listCondition(listCondition: Condition) {
//        this.listCondition = listOf(listCondition)
//    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTemp = itemView.findViewById<TextView>(R.id.tvTemp)
        var tvTime = itemView.findViewById<TextView>(R.id.tvTime)
        var imgTemp = itemView.findViewById<ImageView>(R.id.imgTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_forecast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = listForecast[position]
        val input = SimpleDateFormat("yyyy-MM-dd hh:mm")
        val output = SimpleDateFormat("hh:mm aa")
        try {
            val t: Date = input.parse(forecast.time)
            holder.tvTime.text = output.format(t)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        holder.tvTemp.text = forecast.tempC.toString()
//        val string = condition.text.toString()
//        holder.imgTemp.setIcon(string)
    }

    private fun ImageView.setIcon(string: String) {
        if (string == "Light rain" || string.equals("Moderate rain")) {
            setImageResource(R.drawable.ic_light_rain)
        } else if (string == "Partly cloudy") {
            setImageResource(R.drawable.ic_partly_cloudy)
        } else if (string.equals("Overcast")) {
            setImageResource(R.drawable.ic_overcast)
        } else if (string.equals("Fog")) {
            setImageResource(R.drawable.ic_fog)
        } else if (string.equals("Clear")) {
            setImageResource(R.drawable.ic_clear)
        }
    }

    override fun getItemCount(): Int {
        return listForecast.size
    }
}


