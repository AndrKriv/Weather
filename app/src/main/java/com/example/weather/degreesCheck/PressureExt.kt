package com.example.weather.degreesCheck

    fun String.convertPressure() = (this.toInt() / 1.33).toInt().toString()

