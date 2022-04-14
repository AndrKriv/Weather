package com.example.weather.mvvm.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.mvvm.domain.interactor.WeatherInteractor
import com.example.weather.mvvm.presentation.ForecastUIModel
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val weatherInteractor: WeatherInteractor
) : BaseViewModel() {

    private val _forecastLiveData = MutableLiveData<List<ForecastUIModel>>()
    val forecastLiveData: LiveData<List<ForecastUIModel>> = _forecastLiveData
    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun getForecastData(lat: String, lon: String) {
        weatherInteractor
            .getForecastData(lat, lon)
            .subscribe(
                { forecastWeather ->
                    _forecastLiveData.value = forecastWeather
                }, {
                    _errorLiveData.value = it.message
                })
            .addToDisposable()
    }
}