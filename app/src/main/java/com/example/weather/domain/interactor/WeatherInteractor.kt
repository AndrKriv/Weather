package com.example.weather.domain.interactor

import com.example.weather.core.connection.NetworkStateManager
import com.example.weather.data.database.dao.ForecastDao
import com.example.weather.data.database.dao.TodayDao
import com.example.weather.data.network.api.ApiService
import com.example.weather.extension.*
import com.example.weather.presentation.forecast.model.ForecastUIModel
import com.example.weather.presentation.today.model.TodayUIModel
import kotlinx.coroutines.flow.StateFlow
import java.net.UnknownHostException
import javax.inject.Inject

class WeatherInteractor @Inject constructor(
    private val apiService: ApiService,
    private val todayDao: TodayDao,
    private val forecastDao: ForecastDao,
    val networkStateManager: NetworkStateManager
) {
    fun observeNetworkState(): StateFlow<Boolean> = networkStateManager.state

    suspend fun getTodayData(lat: String, lon: String): TodayUIModel =
        try {
            val todayData = apiService.getTodayData(lat, lon)
            todayData.toEntity().let { todayEntity ->
                todayDao.removeTodayData()
                todayDao.insertTodayData(todayEntity)
            }
            todayData.toUIModel()
        } catch (e: UnknownHostException) { todayDao.getTodayData().toUIModel() }


    suspend fun getForecastData(lat: String, lon: String): List<ForecastUIModel> =
        try {
            val forecastData = apiService.getForecastData(lat, lon).list
            forecastData
                .map { forecastInfo -> forecastInfo.toEntityModel() }
                .let { forecastEntityList ->
                    forecastDao.removeForecastData()
                    forecastDao.insertForecastData(forecastEntityList)
                }
            forecastData.fromInfoToUIModelList()
        } catch (e: UnknownHostException) { forecastDao.getForecastData().fromEntityToUIModelList() }
}