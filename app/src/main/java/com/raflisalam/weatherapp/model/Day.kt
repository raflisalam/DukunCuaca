package com.raflisalam.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Day (
    @SerializedName("totalprecip_mm")
    val precip: Double? = null,
    @SerializedName("daily_chance_of_rain")
    val chanceRainy: Int? = null
)