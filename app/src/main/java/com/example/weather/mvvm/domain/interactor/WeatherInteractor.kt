package com.example.weather.mvvm.domain.interactor

import com.example.weather.mvvm.core.ForecastList
import com.example.weather.mvvm.core.TodayInfo
import com.example.weather.mvvm.data.APIService
import com.example.weather.objects.Constants
import io.reactivex.Single

class WeatherInteractor(private val apiService: APIService) {

    fun getCurrentWeather(lat: String, lon: String): Single<TodayInfo> =
        apiService.getTodayData(
            lat, lon,
            Constants.KEY,
            Constants.UNITS,
            Constants.LANG
        )

    fun getForecastWeather(lat: String, lon: String): Single<ForecastList> =
        apiService.getForecastData(
            lat, lon,
            Constants.KEY,
            Constants.UNITS,
            Constants.LANG
        )
}