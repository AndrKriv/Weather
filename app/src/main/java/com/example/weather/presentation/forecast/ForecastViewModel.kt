package com.example.weather.presentation.forecast

import androidx.lifecycle.viewModelScope
import com.example.weather.core.BaseViewModel
import com.example.weather.domain.interactor.WeatherInteractor
import com.example.weather.presentation.forecast.model.ForecastUIModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val weatherInteractor: WeatherInteractor
) : BaseViewModel() {

    private val _forecastSharedFlow = MutableStateFlow<List<ForecastUIModel>>(emptyList())
    val forecastSharedFlow: StateFlow<List<ForecastUIModel>> = _forecastSharedFlow.asStateFlow()

    private val _errorSharedFlow = MutableSharedFlow<String>(replay = 0)
    val errorSharedFlow: SharedFlow<String> = _errorSharedFlow.asSharedFlow()

    private val _loaderSharedFlow = MutableStateFlow<Boolean>(false)
    val loaderSharedFlow: StateFlow<Boolean> = _loaderSharedFlow.asStateFlow()

    private val _reloadFlow = MutableSharedFlow<Unit>(replay = 0)
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
            .subscribe({ forecastWeather ->
                viewModelScope.launch { _forecastSharedFlow.emit(forecastWeather) }
            }, { viewModelScope.launch { _errorSharedFlow.emit(it.message.toString()) } })
            .addToDisposable()
}