package com.example.weather.mvvm.domain.interactor

import com.example.weather.mvvm.core.TodayInfo
import com.example.weather.mvvm.data.ApiService
import com.example.weather.mvvm.presentation.ForecastUIModel
import com.example.weather.room.dao.WeatherDao
import com.example.weather.utils.fromEntityToUIModelList
import com.example.weather.utils.fromInfoToUIModelList
import com.example.weather.utils.toEntityModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherInteractor @Inject constructor(
    private val apiService: ApiService,
    private val weatherDao: WeatherDao
) {
    fun getCurrentWeather(
        lat: String,
        lon: String
    ): Single<TodayInfo> =
        apiService
            .getTodayData(lat, lon)
            .retry()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getForecastData(lat: String, lon: String): Single<List<ForecastUIModel>> =
        apiService
            .getForecastData(lat, lon)
            .map {
                it.list.map { forecastInfo ->
                    forecastInfo.toEntityModel()
                }
                    .let { forecastEntityList ->
                        if (weatherDao.getTableSize() > 0) {
                            weatherDao.removeData(weatherDao.getForecastData().blockingGet())
                        }
                        weatherDao.insertData(forecastEntityList)
                    }
                it
            }
            .flatMap {
                Single.just(it.list.fromInfoToUIModelList())
            }
            .onErrorResumeNext {
                weatherDao
                    .getForecastData()
                    .filter {
                        weatherDao.getTableSize() > 0
                    }
                    .map {
                        it.fromEntityToUIModelList()
                    }
                    .toSingle()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}