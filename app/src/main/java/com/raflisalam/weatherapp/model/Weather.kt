package com.raflisalam.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("current"  )
    var current  : Current?  = Current(),
    var location : Location? = Location()

)
