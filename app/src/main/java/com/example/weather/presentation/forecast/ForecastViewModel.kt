package com.example.weather.presentation.forecast

import androidx.lifecycle.viewModelScope
import com.example.weather.core.BaseViewModel
import com.example.weather.domain.interactor.WeatherInteractor
import com.example.weather.presentation.forecast.model.ForecastUIModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val weatherInteractor: WeatherInteractor
) : BaseViewModel() {

    private val _forecastStateFlow = MutableStateFlow<List<ForecastUIModel>>(emptyList())
    val forecastStateFlow: StateFlow<List<ForecastUIModel>> = _forecastStateFlow.asStateFlow()

    private val _errorSharedFlow = MutableSharedFlow<String>(replay = 0)
    val errorSharedFlow: SharedFlow<String> = _errorSharedFlow.asSharedFlow()

    private val _loaderStateFlow = MutableStateFlow<Boolean>(false)
    val loaderStateFlow: StateFlow<Boolean> = _loaderStateFlow.asStateFlow()

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

    fun getForecastData(lat: String, lon: String) {
        viewModelScope.launch {
            _loaderStateFlow.value = true
            withContext(Dispatchers.IO) {
                if (weatherInteractor.getForecastData(lat, lon).isNotEmpty()) {
                    _forecastStateFlow.value = weatherInteractor.getForecastData(lat, lon)
                } else {
                    _errorSharedFlow.emit("error")
                }
            }
            _loaderStateFlow.value = false
        }
    }
}