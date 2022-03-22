package com.example.weather.mvvm.data.repository

import com.example.weather.mvvm.domain.models.Location
import com.example.weather.mvvm.domain.repository.LocationInterface

class LocationImpl : LocationInterface {
    override fun getLocation(): Location {
        return Location(55.0, 30.0)
    }
}