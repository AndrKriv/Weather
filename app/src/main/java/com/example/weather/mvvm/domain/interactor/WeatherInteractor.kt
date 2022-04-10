package com.example.weather.mvvm.domain.interactor

import com.example.weather.mvvm.core.TodayInfo
import com.example.weather.mvvm.data.ApiService
import com.example.weather.mvvm.presentation.ForecastUIModel
import com.example.weather.room.dao.WeatherDao
import com.example.weather.room.model.ForecastEntity
import com.example.weather.utils.Constants
import com.example.weather.utils.fromEntityToUIModelList
import com.example.weather.utils.fromInfoToUIModelList
import io.reactivex.Single
import javax.inject.Inject

class WeatherInteractor @Inject constructor(
    private val apiService: ApiService,
    private val weatherDao: WeatherDao
) {
    fun getCurrentWeather(lat: String, lon: String): Single<TodayInfo> =
        apiService.getTodayData(
            lat, lon,
            Constants.KEY,
            Constants.UNITS,
            Constants.LANG
        )

    private fun getForecastWeatherFromInternet(
        lat: String,
        lon: String
    ): Single<List<ForecastUIModel>> =
        apiService.getForecastData(
            lat, lon,
            Constants.KEY,
            Constants.UNITS,
            Constants.LANG
        ).map { it.list.fromInfoToUIModelList() }

    private fun getForecastWeatherFromDatabase(): Single<List<ForecastUIModel>> =
        weatherDao.getForecastData().map { it.fromEntityToUIModelList() }

    private fun insertData(forecastEntity: ForecastEntity) =
        weatherDao.insertData(forecastEntity)

    fun insertDataList(list: List<ForecastEntity>) {
        for (i in list) {
            insertData(i)
        }
    }

    fun getForecastData(
        lat: String,
        lon: String,
        boolean: Boolean
    ): Single<List<ForecastUIModel>> =
        Single.fromCallable {
            if (boolean) {
                return@fromCallable getForecastWeatherFromInternet(lat, lon).blockingGet()
            } else
                return@fromCallable getForecastWeatherFromDatabase().blockingGet()
        }
}