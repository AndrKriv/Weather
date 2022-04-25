package com.example.weather.mvvm.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.mvvm.domain.interactor.WeatherInteractor
import com.example.weather.mvvm.presentation.ForecastUIModel
import com.example.weather.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val weatherInteractor: WeatherInteractor
) : BaseViewModel() {

    private val _forecastLiveData = MutableLiveData<List<ForecastUIModel>>()
    val forecastLiveData: LiveData<List<ForecastUIModel>> = _forecastLiveData
    val errorLiveData = SingleLiveEvent<String>()
    val loaderLiveData = SingleLiveEvent<Boolean>()
    val reloadLiveData: SingleLiveEvent<Unit> = SingleLiveEvent()

    init {
        weatherInteractor
            .observeNetworkState()
            .skip(1)
            .filter {
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                reloadLiveData.call()
            }, {})
            .addToDisposable()
    }

    fun getForecastData(lat: String, lon: String) {
        weatherInteractor
            .getForecastData(lat, lon)
            .doOnSubscribe {
                loaderLiveData.value = true
            }
            .doAfterTerminate {
                loaderLiveData.value = false
            }
            .subscribe(
                { forecastWeather ->
                    _forecastLiveData.value = forecastWeather
                }, {
                    errorLiveData.value = it.message
                })
            .addToDisposable()
    }
}