package com.example.weather.mvvm.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.mvvm.core.ForecastList
import com.example.weather.mvvm.domain.interactor.WeatherInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val interactor: WeatherInteractor) :
    ViewModel() {

    private val disposable = CompositeDisposable()
    private val _forecastLiveData = MutableLiveData<ForecastList>()
    val forecastLiveData: LiveData<ForecastList> = _forecastLiveData
    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun getForecastData(lat: String, lon: String) {
        disposable.add(
            interactor.getForecastWeather(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ forecastWeather ->
                    _forecastLiveData.value = forecastWeather
                }, {
                    _errorLiveData.value = it.message
                })
        )
    }
}