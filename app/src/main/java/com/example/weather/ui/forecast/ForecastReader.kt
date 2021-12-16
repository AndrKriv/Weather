package com.example.weather.ui.forecast

import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.net.URL

fun urlForecast(lat:String,lon:String): Observable<ArrayList<ForecastInfo>> {
    return Observable.create { subscriber ->
        var newsList: ArrayList<ForecastInfo> = ArrayList<ForecastInfo>()

        val key: String = "0cb9d40b9e13129cc06ee1089c96d455"
        val newURL =
            "https://api.openweathermap.org/data/2.5/forecast?lat=$lat&lon=$lon&appid=$key&units=metric&lang=ru"
        val api = URL(newURL).readText()
        val weath = JSONObject(api).getJSONArray("list")
        val count = JSONObject(api).getInt("cnt")
        for (i in 0..count - 1) {
            val description =
                weath.getJSONObject(i).getJSONArray("weather").getJSONObject(0)
                    .getString("description")
            val degrees = weath.getJSONObject(i).getJSONObject("main").getString("temp")
            val time = weath.getJSONObject(i).getString("dt_txt")

            newsList.add(ForecastInfo(time, description, degrees + "°С"))
            subscriber.onNext(newsList)
        }
    }
}