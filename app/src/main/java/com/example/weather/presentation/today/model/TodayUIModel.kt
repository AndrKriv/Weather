package com.example.weather.presentation.today.model

data class TodayUIModel(
    val city: String,
    val date: String,
    val degrees: Double,
    val description: String,
    val pressure: Int,
    val humidity: Double,
    val wind: Int,
    val windSpeed: Double
)