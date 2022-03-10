package com.example.weather.mvvm.domain.repository

import com.example.weather.mvvm.domain.models.Location

interface LocationInterface {

    fun getLocation(): Location
}