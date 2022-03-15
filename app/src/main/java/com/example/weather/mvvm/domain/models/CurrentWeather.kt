package com.example.weather.mvvm.domain.models

class CurrentWeather(
    val city: String,
    //val date: String,
    val degrees: Double,
    val description: String,
    val humidity:Double,
    val pressure: Double,
    val windSpeed: Double
)