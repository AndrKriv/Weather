package com.example.weather.mvvm.core

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TodayInfo(
    @SerializedName("name")
    @Expose
    val city: String,
    @SerializedName("dt")
    @Expose
    val date: String,
    @SerializedName("weather")
    @Expose
    val weather: List<TodayDescription>,
    @SerializedName("main")
    @Expose
    val main: Main,
    @SerializedName("wind")
    @Expose
    val wind: Wind
)

data class TodayDescription(
    @SerializedName("description")
    @Expose
    val description: String
)

data class Main(
    @SerializedName("temp")
    @Expose
    val temp: String,
    @SerializedName("pressure")
    @Expose
    val pressure: String,
    @SerializedName("humidity")
    @Expose
    val humidity: String
)

data class Wind(
    @SerializedName("speed")
    @Expose
    val speed: String,
    @SerializedName("deg")
    @Expose
    val deg: Int
)