package com.raflisalam.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("last_updated")
    var lastUpdated: String? = null,
    @SerializedName("temp_c")
    var tempC: Double? = null,
    var condition: Condition? = Condition(),
    @SerializedName("wind_mph")
    var wind: Double? = null,
    @SerializedName("pressure_mb")
    var pressure: Int? = null,
    var humidity: Int? = null,
    var cloud: Int? = null,
    @SerializedName("air_quality")
    var airQuality: AirQuality? = AirQuality()

)
