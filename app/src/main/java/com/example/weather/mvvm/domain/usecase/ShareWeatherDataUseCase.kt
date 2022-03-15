package com.example.weather.mvvm.domain.usecase

import com.example.weather.mvvm.domain.models.CurrentWeather

class ShareWeatherDataUseCase {
    fun execute(param:CurrentWeather):String{
        return """
            В ${param.city} сейчас ${param.degrees}°C, на улице ${param.description}, скорость ветра ${param.windSpeed} м/с.
            Влажность ${param.humidity}%, давление ${param.pressure} мм рт. ст.
        """.trimIndent()
    }
}