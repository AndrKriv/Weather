package com.example.weather.degreesCheck

object PressureConvertor {
    fun convert(press:Int):String{
        val newPress = press/1.33
        val pressure = newPress.toInt().toString()
        return pressure
    }
}