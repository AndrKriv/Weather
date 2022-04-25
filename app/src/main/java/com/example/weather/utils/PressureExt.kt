package com.example.weather.utils

fun String.convertPressure() = (this.toInt() / 1.33).toInt()