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
            val sky = weath.getJSONObject(0).getString("description")

            val main = JSONObject(api).getJSONObject("main")
            val temp = main.getString("temp")

            val set_rise = JSONObject(api).getJSONObject("sys")

            val start_sec = set_rise.getInt("sunrise")
            val last_sec = set_rise.getInt("sunset")

            val time_of_the_day = last_sec - start_sec
            val hour_time_of_the_day: Int = time_of_the_day / 3600
            val min_time_of_the_day: Int = (time_of_the_day % 3600) / 60

            val only_date = SimpleDateFormat("dd.MM.yyyy")
            val only_time = SimpleDateFormat("HH:mm")
            val only_date_ = only_date.format(Date())
            val only_time_ = only_time.format(Date())
            subscriber.onNext(listOf("$city", "$temp",  "$only_date_"))
        }
    }



}


