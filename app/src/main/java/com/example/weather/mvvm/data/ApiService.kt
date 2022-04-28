package com.example.weather.mvvm.data

import com.example.weather.mvvm.core.ForecastList
import com.example.weather.mvvm.core.TodayInfo
import com.example.weather.utils.Constants
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather?")
    fun getTodayData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String = Constants.KEY,
        @Query("units") units: String = Constants.UNITS,
        @Query("lang") lang: String = Constants.LANG
    ): Single<TodayInfo>

    @GET("forecast?")
    fun getForecastData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String = Constants.KEY,
        @Query("units") units: String = Constants.UNITS,
        @Query("lang") lang: String = Constants.LANG
    ): Single<ForecastList>
}