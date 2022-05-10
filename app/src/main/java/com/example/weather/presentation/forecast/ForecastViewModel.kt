package com.example.weather.presentation.forecast

import androidx.lifecycle.viewModelScope
import com.example.weather.core.BaseViewModel
import com.example.weather.domain.interactor.WeatherInteractor
import com.example.weather.presentation.forecast.model.ForecastUIModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val weatherInteractor: WeatherInteractor
) : BaseViewModel() {

    private val _forecastSharedFlow = MutableSharedFlow<List<ForecastUIModel>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val forecastSharedFlow: SharedFlow<List<ForecastUIModel>> = _forecastSharedFlow.asSharedFlow()

    private val _errorSharedFlow = MutableSharedFlow<String>()
    val errorSharedFlow: SharedFlow<String> = _errorSharedFlow.asSharedFlow()

    private val _loaderSharedFlow = MutableSharedFlow<Boolean>()
    val loaderSharedFlow: SharedFlow<Boolean> = _loaderSharedFlow.asSharedFlow()

    private val _reloadFlow = MutableSharedFlow<Unit>()
    val reloadFlow: SharedFlow<Unit> = _reloadFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            weatherInteractor
                .observeNetworkState()
                .drop(1)
                .filter { isConnected -> isConnected }
                .collect { _reloadFlow.emit(Unit) }
        }
    }

    fun getForecastData(lat: String, lon: String) =
        weatherInteractor
            .getForecastData(lat, lon)
            .doOnSubscribe { viewModelScope.launch { _loaderSharedFlow.emit(true) } }
            .doAfterTerminate { viewModelScope.launch { _loaderSharedFlow.emit(false) } }
            .subscribe(
                { forecastWeather ->
                    viewModelScope.launch { _forecastSharedFlow.emit(forecastWeather) }
                }, { viewModelScope.launch { _errorSharedFlow.emit(it.message.toString()) } })
            .addToDisposable()
}