package com.example.weather.mvvm.domain.usecase

import com.example.weather.mvvm.domain.models.CurrentWeather

class GetCurrentWeatherUseCase {

    fun execute(): CurrentWeather {
        return CurrentWeather(
            city = "Vtb",
            date = "10.01.2000",
            degrees = 0.0,
            description = "ясно",
            humidity = 1.0,
            pressure = 50.0,
            windSpeed = 1.0
        )
    }
}