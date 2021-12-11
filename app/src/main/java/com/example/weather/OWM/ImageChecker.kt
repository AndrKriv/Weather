package com.example.weather.OWM

import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.weather.R

object ImageChecker{
    fun imageWeather(stringChecker:String):Int{
        return when(stringChecker) {
            "ясно" -> R.drawable.sun
            "пасмурно" -> R.drawable.cloudy
            "облачно с прояснениями" -> R.drawable.cloud_sun
            "небольшая облачность" -> R.drawable.cloud
            "переменная облачность" -> R.drawable.cloud
            "сильный дождь" -> R.drawable.rain
            "дождь" -> R.drawable.rain
            "небольшой дождь" -> R.drawable.rain_small
            "проливной дождь" -> R.drawable.rain
            else -> R.drawable.unknown

        }
    }
}