package com.example.weather.mvvm.domain.usecase

import com.example.weather.mvvm.domain.models.Forecast

class GetAForecastForFiveDaysUseCase {

    fun execute(): Forecast {
        return Forecast(date = "10.01.2000", degrees = 0.0, description = "ясно")
    }
}