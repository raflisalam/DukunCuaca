package com.raflisalam.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("name")
    var city: String? = null,
    var region: String? = null,
    var country: String? = null,
    @SerializedName("localtime")
    var dateTime: String? = null
)
