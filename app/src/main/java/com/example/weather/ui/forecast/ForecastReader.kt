package com.example.weather.ui.forecast

import com.example.weather.objects.toAllProject
import com.example.weather.parseDate
import io.reactivex.Observable
import org.json.JSONObject
import java.net.URL

fun urlForecast(lat:String,lon:String): Observable<ArrayList<ForecastInfo>> {
    return Observable.create { subscriber ->
        var newsList: ArrayList<ForecastInfo> = ArrayList<ForecastInfo>()
        val URL = "https://api.openweathermap.org/data/2.5/forecast?lat=$lat&lon=$lon&appid=${toAllProject.key}&units=metric&lang=ru"
        val api = URL(URL).readText()
        val weath = JSONObject(api).getJSONArray("list")

        val count = JSONObject(api).getInt("cnt")
        for (i in 0..count - 1) {
            val description =
                weath.getJSONObject(i).getJSONArray("weather").getJSONObject(0)
                    .getString("description")
            val degrees = weath.getJSONObject(i).getJSONObject("main").getString("temp")
            val tm = weath.getJSONObject(i).getString("dt_txt")
            val time = parseDate(tm)
            newsList.add(ForecastInfo(time, description, degrees + "°С"))
        }
        subscriber.onNext(newsList)
    }
}