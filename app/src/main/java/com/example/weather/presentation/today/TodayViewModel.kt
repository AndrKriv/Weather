package com.example.weather.presentation.today

import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.example.weather.core.BaseViewModel
import com.example.weather.domain.interactor.WeatherInteractor
import com.example.weather.presentation.today.model.TodayUIModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodayViewModel @Inject constructor(
    private val weatherInteractor: WeatherInteractor
) : BaseViewModel() {

    private val _todayStateFlow = MutableStateFlow<TodayUIModel?>(null)
    val todayStateFlow: StateFlow<TodayUIModel?> = _todayStateFlow.asStateFlow()

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

    fun getTodayData(lat: String, lon: String) {
        viewModelScope.launch {
            _loaderStateFlow.value = true
            withContext(Dispatchers.IO) {
                try {
                    _todayStateFlow.value = weatherInteractor.getTodayData(lat, lon)
                } catch (e: NullPointerException) { _errorSharedFlow.emit("error") }
            }
            _loaderStateFlow.value = false
        }
    }

    fun sendInfoChooser(messageText: String): Intent =
        Intent.createChooser(
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, messageText)
                type = "text/plain"
            },
            "Launch"
        )

    fun stringToShare(
        city: String,
        degrees: Double,
        description: String,
        windSpeed: Double,
        humidity: Double,
        pressure: Int
    ) =
        """
    В $city сейчас $degrees°C, на улице $description, скорость ветра $windSpeed м/с.
    Влажность $humidity%, давление $pressure мм рт. ст.
    """.trimIndent()
}