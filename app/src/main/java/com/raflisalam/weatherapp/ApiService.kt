package com.raflisalam.weatherapp

import com.raflisalam.weatherapp.model.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("http://api.weatherapi.com/v1/forecast.json?key=a2fda04aafca46879bd150313212211&days=1&aqi=yes")
    fun getWeather(
        @Query("q") query: String
    ): Call<Weather>
}