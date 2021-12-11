package com.example.weather.ui.today

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.Observable.create
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


open class TodayWeatherReader() {

    val city: String = "London"
    val key: String = "0cb9d40b9e13129cc06ee1089c96d455"
    val url: String = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$key&units=metric&lang=ru"

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun urlTodayWeather():Observable<List<String>>{

        return create { subscriber ->

            val api = URL(url).readText()

            val weath = JSONObject(api).getJSONArray("weather")
            val description = weath.getJSONObject(0).getString("description")

            val main = JSONObject(api).getJSONObject("main")
            val temp = main.getString("temp")

            val setRise = JSONObject(api).getJSONObject("sys")

            val startSec = setRise.getInt("sunrise")
            val lastSec = setRise.getInt("sunset")

            val timeOfTheDay = lastSec - startSec
            val hourTimeOfTheDay: Int = timeOfTheDay / 3600
            val minTimeOfTheDay: Int = (timeOfTheDay % 3600) / 60

            val onlyDate = SimpleDateFormat("dd.MM.yyyy").format(Date())
            val onlyTime = SimpleDateFormat("HH:mm").format(Date())
            subscriber.onNext(listOf(city, temp, description, onlyTime+" "+onlyDate))
        }
    }



}


