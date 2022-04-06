package com.example.weather.mvvm.domain.interactor

import com.example.weather.room.dao.WeatherDao
import com.example.weather.room.model.ForecastDatabaseModel
import io.reactivex.Single
import javax.inject.Inject

class DatabaseInteractor @Inject constructor(private val weatherDao: WeatherDao) {

    fun getForecastWeather(): Single<List<ForecastDatabaseModel>> =
        weatherDao.getForecastInfo()

    fun insertData(forecastDatabaseModel: ForecastDatabaseModel) =
        weatherDao.insertData(forecastDatabaseModel)
}
