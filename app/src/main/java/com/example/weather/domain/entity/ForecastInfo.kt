package com.example.weather.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForecastList(
    @SerializedName("list")
    @Expose
    var list: List<ForecastInfo>
)

data class ForecastInfo(
    @SerializedName("dt_txt")
    val time: String,
    @SerializedName("weather")
    @Expose
    val weather: List<Description>,
    @SerializedName("main")
    @Expose
    var temp: Temp
)

data class Description(
    @SerializedName("description")
    @Expose
    val description: String
)

data class Temp(
    @SerializedName("temp")
    @Expose
    var degrees: String
)