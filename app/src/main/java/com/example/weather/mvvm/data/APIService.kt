package com.example.weather.mvvm.data

import com.example.weather.mvvm.core.ForecastList
import com.example.weather.mvvm.core.TodayInfo
import com.example.weather.mvvm.data.repository.LocationImpl
import com.example.weather.mvvm.domain.usecase.GetLocationUseCase
import com.example.weather.objects.Constants
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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

    companion object Factory{
        fun create(): APIService {
            return Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(APIService::class.java)
        }
    }
}

class ObservableRepository(private val apiService: APIService) {
    fun todayData(lat:String,lon:String): Observable<TodayInfo> {
        return apiService.getTodayData(lat,lon,
            Constants.KEY,
            "metric",
            "ru")
    }
    fun forecastData(lat:String,lon:String): Observable<ForecastList> {
        return apiService.getForecastData(lat,lon,
            Constants.KEY,
            "metric",
            "ru")
    }
}