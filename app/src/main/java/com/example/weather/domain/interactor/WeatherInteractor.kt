package com.example.weather.domain.interactor

import com.example.weather.core.connection.NetworkStateManager
import com.example.weather.data.database.dao.ForecastDao
import com.example.weather.data.database.dao.TodayDao
import com.example.weather.data.network.api.ApiService
import com.example.weather.extension.*
import com.example.weather.presentation.forecast.model.ForecastUIModel
import com.example.weather.presentation.today.model.TodayUIModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class WeatherInteractor @Inject constructor(
    private val apiService: ApiService,
    private val todayDao: TodayDao,
    private val forecastDao: ForecastDao,
    val networkStateManager: NetworkStateManager
) {
    fun observeNetworkState(): StateFlow<Boolean> = networkStateManager.state

    fun getTodayData(
        lat: String,
        lon: String
    ): Flow<TodayUIModel> =
        flow {
            val result = apiService.getTodayData(lat, lon)
            result.toEntity().let { todayEntity ->
                todayDao.removeTodayData()
                todayDao.insertTodayData(todayEntity)
            }
            emit(result.toUIModel())
        }
            .catch {
                val databaseData =
                    flowOf(todayDao.getTodayData()).map { todayEntity -> todayEntity.toUIModel() }
                emitAll(databaseData)
            }

    fun getForecastData(lat: String, lon: String): Flow<List<ForecastUIModel>> =
        flow {
            val result = apiService.getForecastData(lat, lon).list
            result.map { forecastInfo ->
                forecastInfo.toEntityModel()
            }.let { forecastEntityList ->
                forecastDao.removeForecastData()
                forecastDao.insertForecastData(forecastEntityList)
            }
            emit(result.fromInfoToUIModelList())
        }
            .catch {
                val databaseData =
                    flowOf(forecastDao.getForecastData())
                        .map { forecastEntityList -> forecastEntityList.fromEntityToUIModelList() }
                emitAll(databaseData)
            }
}