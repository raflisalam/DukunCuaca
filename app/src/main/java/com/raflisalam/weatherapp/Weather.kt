package com.raflisalam.weatherapp

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("current"  )
    var current  : Current?  = Current()

)
