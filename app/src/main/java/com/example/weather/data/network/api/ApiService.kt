package com.example.weather.data.network.api

import com.example.weather.core.Constants
import com.example.weather.domain.entity.ForecastList
import com.example.weather.domain.entity.TodayInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather?")
    suspend fun getTodayData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String = Constants.KEY,
        @Query("units") units: String = Constants.UNITS,
        @Query("lang") lang: String = Constants.LANG
    ): TodayInfo

    @GET("forecast?")
    suspend fun getForecastData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String = Constants.KEY,
        @Query("units") units: String = Constants.UNITS,
        @Query("lang") lang: String = Constants.LANG
    ): ForecastList
}