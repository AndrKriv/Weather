package com.example.weather.ui.today

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.Observable.create
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

    @SuppressLint("SetTextI18n", "SimpleDateFormat")

    fun urlTodayWeather(lat:String,lon:String):Observable<List<String>>{
//        val city: String = "London"
//        val url: String = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$key&units=metric&lang=ru"
        return create { subscriber ->
            val key: String = "0cb9d40b9e13129cc06ee1089c96d455"
            val newURL: String = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$key&units=metric&lang=ru"

            val api = URL(newURL).readText()

            val weath = JSONObject(api).getJSONArray("weather")
            val description = weath.getJSONObject(0).getString("description")

            val city = JSONObject(api).getString("name")

            val main = JSONObject(api).getJSONObject("main")
            val temp = main.getString("temp")

            val onlyDate = SimpleDateFormat("dd.MM.yyyy").format(Date())
            val onlyTime = SimpleDateFormat("HH:mm").format(Date())
            subscriber.onNext(listOf(city, temp, description, onlyTime+" "+onlyDate))
            subscriber.onComplete()
        }
    }