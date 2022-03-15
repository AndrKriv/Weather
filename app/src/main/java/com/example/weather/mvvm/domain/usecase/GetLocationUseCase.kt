package com.example.weather.mvvm.domain.usecase

import com.example.weather.mvvm.domain.models.Location
import com.example.weather.mvvm.domain.repository.LocationInterface

class GetLocationUseCase(private val locationInterface: LocationInterface) {

    fun execute(): Location {
        return locationInterface.getLocation()
    }
}