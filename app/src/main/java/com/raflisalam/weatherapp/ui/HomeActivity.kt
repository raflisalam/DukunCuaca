package com.raflisalam.weatherapp.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import com.raflisalam.weatherapp.ApiClient
import com.raflisalam.weatherapp.R
import com.raflisalam.weatherapp.model.Weather
import com.raflisalam.weatherapp.databinding.ActivityHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searching.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val string = query.toString()
                getWeather(string)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun getWeather(query: String) {
        ApiClient.instance.getWeather(query).enqueue(object : Callback<Weather> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                binding.apply {
                    tvTemp.text = response.body()?.current?.tempC.toString() + "°"
                    tvCondition.text = response.body()?.current?.condition?.text.toString()
                    tvLocation.text = response.body()?.location?.city.toString() +", "+ response.body()?.location?.region.toString()
                    dateTime.text = response.body()?.location?.dateTime.toString()
                    val string = response.body()?.current?.condition?.text.toString()
                    setIcon(string)
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                t.message?.let { Log.d("Fail Load!", it) }
            }

        })
    }

    private fun setIcon(string: String) {
        if (string.equals("Light rain") || string.equals("Moderate rain")) {
            binding.iconTemp.setImageResource(R.drawable.ic_light_rain)
        } else if (string.equals("Partly cloudy")) {
            binding.iconTemp.setImageResource(R.drawable.ic_partly_cloudy)
        } else if (string.equals("Overcast")) {
            binding.iconTemp.setImageResource(R.drawable.ic_overcast)
        } else if (string.equals("Fog")) {
            binding.iconTemp.setImageResource(R.drawable.ic_fog)
        } else if (string.equals("Clear")) {
            binding.iconTemp.setImageResource(R.drawable.ic_clear)
        }
    }

}