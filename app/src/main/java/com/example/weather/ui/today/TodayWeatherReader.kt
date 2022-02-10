package com.example.weather.ui.today

import com.example.weather.dateFormat.today
import com.example.weather.degreesCheck.WindDirection
import com.example.weather.degreesCheck.convertPressure
import com.example.weather.objects.Constants
import io.reactivex.Observable
import io.reactivex.Observable.create
import org.json.JSONObject
import java.net.URL

fun urlTodayWeather(latitude: String, longitude: String): Observable<List<String>> {
    return create { subscriber ->
        val url =
            "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=${Constants.KEY}&units=metric&lang=ru"
        val api = URL(url).readText()
        val weatherStorage = JSONObject(api).getJSONArray("weather")
        val description = weatherStorage.getJSONObject(0).getString("description")
        val main = JSONObject(api).getJSONObject("main")
        val temp = main.getString("temp")
        val pressureReader = main.getInt("pressure")
        val pressure = pressureReader.convertPressure()
        val humidity = main.getString("humidity")
        val wind = JSONObject(api).getJSONObject("wind")
        val windSpeed = wind.getString("speed")
        val degrees = wind.getInt("deg")
        val windD = WindDirection.windDirection(degrees).toString()
        val city = JSONObject(api).getString("name")
        subscriber.onNext(
            listOf(
                city,
                temp,
                description,
                today(),
                pressure,
                humidity,
                windD,
                windSpeed
            )
        )
    }
}