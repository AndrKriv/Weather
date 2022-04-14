package com.example.weather.mvvm.domain.interactor

import android.util.Log
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
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getForecastData(lat: String, lon: String): Single<List<ForecastUIModel>> =
        apiService
            .getForecastData(lat, lon)
            .map {
                it.list.map { forecastInfo ->
                    forecastInfo.toEntityModel()
                }
                    .let { forecastList ->
                        Log.e("AAA", weatherDao.getTableSize().toString())
                        if (weatherDao.getTableSize() > 0) {
                            weatherDao.removeData(weatherDao.getForecastData().blockingGet())
                        }
                        weatherDao.insertData(forecastList)
                    }
                it
            }
            .flatMap {
                Single.just(it.list.fromInfoToUIModelList())
            }
            .onErrorResumeNext {
                weatherDao.getForecastData().map {
                    it.fromEntityToUIModelList()
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}