package com.example.weather.mvvm.domain.interactor

import com.example.weather.mvvm.data.ApiService
import com.example.weather.mvvm.domain.connection.NetworkStateManager
import com.example.weather.mvvm.presentation.ForecastUIModel
import com.example.weather.mvvm.presentation.TodayUIModel
import com.example.weather.room.dao.ForecastDao
import com.example.weather.room.dao.TodayDao
import com.example.weather.utils.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class WeatherInteractor @Inject constructor(
    private val apiService: ApiService,
    private val todayDao: TodayDao,
    private val forecastDao: ForecastDao,
    val networkStateManager: NetworkStateManager
) {
    fun observeNetworkState(): BehaviorSubject<Boolean> =
        networkStateManager.connectionObserver

    fun getCurrentWeather(
        lat: String,
        lon: String
    ): Single<TodayUIModel> =
        apiService
            .getTodayData(lat, lon)
            .map { todayInfo ->
                todayInfo.toEntity().let { todayEntity ->
                    todayDao.removeTodayData()
                    todayDao.insertTodayData(todayEntity)
                }
                todayInfo.toUIModel()
            }
            .onErrorResumeNext {
                todayDao
                    .getTodayData()
                    .map { todayEntity ->
                        todayEntity.toUIModel()
                    }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getForecastData(lat: String, lon: String): Single<List<ForecastUIModel>> =
        apiService
            .getForecastData(lat, lon)
            .map { forecastList ->
                forecastList.list.map { forecastInfo ->
                    forecastInfo.toEntityModel()
                }
                    .let { forecastEntityList ->
                        forecastDao.removeForecastData()
                        forecastDao.insertForecastData(forecastEntityList)
                    }
                forecastList.list.fromInfoToUIModelList()
            }
            .onErrorResumeNext {
                forecastDao
                    .getForecastData()
                    .map { forecastEntityList ->
                        forecastEntityList.fromEntityToUIModelList()
                    }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}