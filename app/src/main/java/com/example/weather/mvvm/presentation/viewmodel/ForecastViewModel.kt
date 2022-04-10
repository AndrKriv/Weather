package com.example.weather.mvvm.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.mvvm.domain.interactor.WeatherInteractor
import com.example.weather.mvvm.presentation.ForecastUIModel
import com.example.weather.utils.fromUIToEntityList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val weatherInteractor: WeatherInteractor
) : BaseViewModel() {

    private val _forecastLiveData = MutableLiveData<List<ForecastUIModel>>()
    val forecastLiveData: LiveData<List<ForecastUIModel>> = _forecastLiveData
    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun getForecastData(lat: String, lon: String, boolean: Boolean) {
        weatherInteractor
            .getForecastData(lat, lon, boolean)
            .doOnSuccess {
                weatherInteractor.insertDataList(it.fromUIToEntityList())
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ forecastWeather ->
                _forecastLiveData.value = forecastWeather
            }, {
                _errorLiveData.value = it.message
            })
            .addToDisposable()
    }
}