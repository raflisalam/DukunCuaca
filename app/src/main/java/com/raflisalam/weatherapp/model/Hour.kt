package com.raflisalam.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Hour(
    var time: String? = null,
    @SerializedName("temp_c")
    var tempC: Double? = null,
    @SerializedName("condition")
    var condition: Condition? = Condition(),
)
