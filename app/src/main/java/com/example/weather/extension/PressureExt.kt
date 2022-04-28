package com.example.weather.extension

fun String.convertPressure() = (this.toInt() / 1.33).toInt()