package com.raflisalam.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raflisalam.weatherapp.ApiClient
import com.raflisalam.weatherapp.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel: ViewModel() {

    val listForecast = MutableLiveData<List<Hour>>()
    val listDay = MutableLiveData<Day>()
    val weatherData = MutableLiveData<Weather>()

    fun setForecast(query: String) {
        ApiClient.instance.getWeather(query).enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                if (response.isSuccessful) {
                    weatherData.postValue(response.body())
                    val forecast = response.body()?.forecast!!
                    val forecastDay: List<Forecastday> = forecast.forecastday
                    for (data in forecastDay) {
                        listDay.postValue(data.day!!)
                        val hourr: List<Hour> = data.hour
                        for (hour in hourr) {
                            listForecast.postValue(hourr)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                t.message?.let { Log.d("Fail Load!", it) }
            }

        })
    }

    fun getDay(): LiveData<Day> {
        return listDay
    }

    fun getWeather(): LiveData<Weather> {
        return weatherData
    }

    fun getForecast(): LiveData<List<Hour>> {
        return listForecast
    }
}