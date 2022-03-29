package com.example.weather.mvvm.domain.interactor

import com.example.weather.mvvm.core.ForecastList
import com.example.weather.mvvm.core.TodayInfo
import com.example.weather.mvvm.data.ApiService
import com.example.weather.utils.Constants
import io.reactivex.Single
import javax.inject.Inject

class WeatherInteractor @Inject constructor(private val apiService: ApiService) {
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