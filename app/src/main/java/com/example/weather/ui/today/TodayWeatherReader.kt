package com.example.weather.ui.today

import com.example.weather.degreesCheck.PressureConvertor
import com.example.weather.degreesCheck.PressureConvertor.convertPressure
import com.example.weather.degreesCheck.WindDirection
import com.example.weather.objects.Constants
import com.example.weather.today
import io.reactivex.Observable
import io.reactivex.Observable.create
import org.json.JSONObject
import java.net.URL

fun urlTodayWeather(lat: String, lon: String): Observable<List<String>> {
    return create { subscriber ->
        val url =
            "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=${Constants.KEY}&units=metric&lang=ru"
        val api = URL(url).readText()
        val weather = JSONObject(api).getJSONArray("weather")
        val description = weather.getJSONObject(0).getString("description")
        val main = JSONObject(api).getJSONObject("main")
        val temp = main.getString("temp")
        val press = main.getInt("pressure")
        val pressure = press.convertPressure()
        val humidity = main.getString("humidity")
        val wind = JSONObject(api).getJSONObject("wind")
        val windSpeed = wind.getString("speed")
        val deg = wind.getInt("deg")
        val windD = WindDirection.windDirection(deg)
        val city = JSONObject(api).getString("name")
        subscriber.onNext(
            listOf(
                city,
                temp,
                description,
                today(),
                pressure,
                humidity,
                windD.toString(),
                windSpeed
            )
        )
    }
}