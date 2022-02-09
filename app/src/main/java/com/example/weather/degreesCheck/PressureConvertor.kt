package com.example.weather.degreesCheck

object PressureConvertor {
    fun Int.convertPressure() = (this / 1.33).toInt().toString()
}
