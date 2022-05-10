package com.example.weather.presentation.today

import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.example.weather.core.BaseViewModel
import com.example.weather.domain.interactor.WeatherInteractor
import com.example.weather.presentation.today.model.TodayUIModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodayViewModel @Inject constructor(
    private val weatherInteractor: WeatherInteractor
) : BaseViewModel() {

    private val _todaySharedFlow = MutableSharedFlow<TodayUIModel>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val todaySharedFlow: SharedFlow<TodayUIModel> = _todaySharedFlow.asSharedFlow()

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

    fun getTodayData(lat: String, lon: String) =
        weatherInteractor
            .getTodayData(lat, lon)
            .doOnSubscribe { viewModelScope.launch { _loaderSharedFlow.emit(true) } }
            .doAfterTerminate { viewModelScope.launch { _loaderSharedFlow.emit(false) } }
            .subscribe({ currentWeather ->
                viewModelScope.launch { _todaySharedFlow.emit(currentWeather) }
            }, { viewModelScope.launch { _errorSharedFlow.emit(it.message.toString()) } })
            .addToDisposable()


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