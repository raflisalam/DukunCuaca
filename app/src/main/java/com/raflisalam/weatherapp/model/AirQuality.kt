package com.raflisalam.weatherapp.model

import com.google.gson.annotations.SerializedName

data class AirQuality(
    @SerializedName("no2")
    var no2: Double? = null,
    @SerializedName("o3")
    var o3: Double? = null,
    @SerializedName("pm2_5")
    var pm25: Double? = null,
    @SerializedName("gb-defra-index")
    var aqiIndex: Int? = null
)
