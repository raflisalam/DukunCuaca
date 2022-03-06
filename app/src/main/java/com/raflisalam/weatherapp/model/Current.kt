package com.raflisalam.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("temp_c")
    var tempC: Int? = null,
    var condition: Condition? = Condition(),
    @SerializedName("wind_mph")
    var wind: Double? = null,
    @SerializedName("pressure_mb")
    var pressure: Int? = null,
    var humidity: Int? = null,
    @SerializedName("air_quality")
    var airQuality: AirQuality? = AirQuality()

)
