package com.raflisalam.weatherapp.model

import com.google.gson.annotations.SerializedName
import com.raflisalam.weatherapp.model.Condition

data class Current(
    @SerializedName("temp_c")
    var tempC: Int? = null,
    var condition: Condition? = Condition(),
)
