package com.example.weather.OWM

import com.example.weather.R

object ImageChecker{//мапинг данных
    //single responcibility -не сюда
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
            "небольшой снег" -> R.drawable.small_snow
            "снег" -> R.drawable.snowing
            "снег с дождем" -> R.drawable.snow_rain
            "туман"->R.drawable.fog
            else -> R.drawable.unknown

        }
    }
}