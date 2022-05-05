package com.example.weather.presentation.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.core.BaseViewModel
import com.example.weather.core.SingleLiveEvent
import com.example.weather.domain.interactor.WeatherInteractor
import com.example.weather.presentation.forecast.model.ForecastUIModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val weatherInteractor: WeatherInteractor
) : BaseViewModel() {

    private val _forecastLiveData = MutableLiveData<List<ForecastUIModel>>()
    val forecastLiveData: LiveData<List<ForecastUIModel>> = _forecastLiveData
    val errorLiveData = SingleLiveEvent<String>()
    val loaderLiveData = SingleLiveEvent<Boolean>()
    private val _reloadFlow = MutableSharedFlow<Unit>()
    val reloadFlow: SharedFlow<Unit> = _reloadFlow.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            weatherInteractor
                .observeStateFlow()
                .drop(1)
                .filter { it }
                .collect {
                    _reloadFlow.emit(Unit)
                }
        }
    }

    fun getForecastData(lat: String, lon: String) {
        weatherInteractor
            .getForecastData(lat, lon)
            .doOnSubscribe { loaderLiveData.value = true }
            .doAfterTerminate { loaderLiveData.value = false }
            .subscribe(
                { forecastWeather ->
                    _forecastLiveData.value = forecastWeather
                }, { errorLiveData.value = it.message })
            .addToDisposable()
    }
}