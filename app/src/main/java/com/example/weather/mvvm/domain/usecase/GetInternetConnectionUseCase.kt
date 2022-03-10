package com.example.weather.mvvm.domain.usecase

class GetInternetConnectionUseCase {

    fun execute(checkPermission: String):Boolean{
        return checkPermission.isNotEmpty()
    }
}