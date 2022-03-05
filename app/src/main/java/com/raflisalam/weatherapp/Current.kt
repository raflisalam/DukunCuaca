package com.raflisalam.weatherapp

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("temp_c")
    var tempC: Int? = null,
    var condition: Condition? = Condition(),
)
