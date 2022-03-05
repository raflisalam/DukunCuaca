package com.raflisalam.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Condition(
    @SerializedName("text" )
    var text : String? = null,
)
