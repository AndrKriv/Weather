package com.example.weather.mvvm.data

import com.example.weather.mvvm.core.ForecastList
import com.example.weather.mvvm.core.TodayInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("forecast?")
    fun getForecastData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): Observable<ForecastList>

    @GET("weather?")
    fun getTodayData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): Observable<TodayInfo>
}