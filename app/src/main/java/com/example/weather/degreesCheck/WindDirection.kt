package com.example.weather.degreesCheck

import com.example.weather.R

object WindDirection {
    fun windDirection(degrees: Int) = when (degrees) {
        in 1..89 -> R.string.ne
        in 91..179 -> R.string.se
        in 181..269 -> R.string.sw
        in 271..359 -> R.string.nw
        90 -> R.string.east
        180 -> R.string.south
        270 -> R.string.west
        else -> R.string.north
    }
}