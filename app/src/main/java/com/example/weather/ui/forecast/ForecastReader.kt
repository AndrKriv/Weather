package com.example.weather.ui.forecast

import com.example.weather.objects.Constants
import com.example.weather.dateFormat.toDate
import io.reactivex.Observable
import org.json.JSONObject
import java.net.URL

fun urlForecast(latitude: String, longitude: String): Observable<ArrayList<ForecastInfo>> {
    return Observable.create { subscriber ->
        val newsList: ArrayList<ForecastInfo> = ArrayList<ForecastInfo>()
        val url =
            "https://api.openweathermap.org/data/2.5/forecast?lat=$latitude&lon=$longitude&appid=${Constants.KEY}&units=metric&lang=ru"
        val api = URL(url).readText()
        val weatherStorage = JSONObject(api).getJSONArray("list")
        val count = JSONObject(api).getInt("cnt")
        for (i in 0 until count) {
            val description =
                weatherStorage.getJSONObject(i).getJSONArray("weather").getJSONObject(0)
                    .getString("description")
            val degrees = weatherStorage.getJSONObject(i).getJSONObject("main").getString("temp")
            val date = weatherStorage.getJSONObject(i).getString("dt_txt")
            val time = date.toDate()
            newsList.add(ForecastInfo(time, description, "$degrees°С"))
        }
        subscriber.onNext(newsList)
    }
}