package com.example.weather.mvvm.domain.usecase

import com.example.weather.R
import com.example.weather.mvvm.domain.models.DescriptionOfTheWeather
import com.example.weather.mvvm.domain.models.WeatherPicture

class GetWeatherPictureUseCase {

    fun execute(param:DescriptionOfTheWeather):WeatherPicture{
//fun execute(param:String): Int {
        return when (param) {
            DescriptionOfTheWeather("ясно") -> WeatherPicture(R.drawable.sun)
//            "пасмурно" -> WeatherPicture(R.drawable.cloudy)
//            "облачно с прояснениями" -> WeatherPicture(R.drawable.cloud_sun)
//            "небольшая облачность" -> WeatherPicture(R.drawable.cloud)
//            "переменная облачность" -> WeatherPicture(R.drawable.cloud)
//            "сильный дождь" -> WeatherPicture(R.drawable.rain)
//            "дождь" -> WeatherPicture(R.drawable.rain)
//            "небольшой дождь" -> WeatherPicture(R.drawable.rain_small)
//            "проливной дождь" -> WeatherPicture(R.drawable.rain)
//            "небольшой снег" -> WeatherPicture(R.drawable.small_snow)
//            "снег" -> WeatherPicture(R.drawable.snowing)
//            "снег с дождем" -> WeatherPicture(R.drawable.snow_rain)
//            "туман" -> WeatherPicture(R.drawable.fog)
            else -> WeatherPicture(R.drawable.unknown)
        }
    }
}