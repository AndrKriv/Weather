package com.example.weather.degreesCheck

object WindDirection {
    fun windDirection(degrees:Int):String{
        when (degrees) {
            in 1..89 -> return "СВ"
            in 91..179 -> return "ЮВ"
            in 181..269 -> return "ЮЗ"
            in 271..359 -> return "СЗ"
            90 -> return "Восточный"
            180 -> return "Южный"
            270 -> return "Западный"
            else -> return "Северный"
        }
    }
}