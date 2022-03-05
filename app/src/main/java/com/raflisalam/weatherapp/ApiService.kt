package com.raflisalam.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("http://api.weatherapi.com/v1/current.json?key=a2fda04aafca46879bd150313212211&")
    fun getWeather(
        @Query("q") query: String
    ): Call<Weather>
}