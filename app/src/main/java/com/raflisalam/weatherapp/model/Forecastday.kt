package com.raflisalam.weatherapp.model

data class Forecastday(
    val day: Day? = Day(),
    val hour : List<Hour>
)
