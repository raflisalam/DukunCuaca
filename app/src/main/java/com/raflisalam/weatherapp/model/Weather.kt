package com.raflisalam.weatherapp.model

import com.google.gson.annotations.SerializedName
import com.raflisalam.weatherapp.model.Current

data class Weather(
    @SerializedName("current"  )
    var current  : Current?  = Current(),
    var location : Location? = Location()

)
