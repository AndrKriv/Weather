package com.example.weather.presentation.today

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.core.BaseViewModel
import com.example.weather.core.SingleLiveEvent
import com.example.weather.domain.interactor.WeatherInteractor
import com.example.weather.presentation.today.model.TodayUIModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodayViewModel @Inject constructor(
    private val weatherInteractor: WeatherInteractor
) : BaseViewModel() {

    private val _todayLiveData = MutableLiveData<TodayUIModel>()
    val todayLiveData: LiveData<TodayUIModel> = _todayLiveData
    val errorLiveData = SingleLiveEvent<String>()
    val loaderLiveData = SingleLiveEvent<Boolean>()

    private val _reloadFlow = MutableSharedFlow<Unit>()
    val reloadFlow: SharedFlow<Unit> = _reloadFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            weatherInteractor
                .observeStateFlow()
                .drop(1)
                .filter {
                    it
                }
                .collect {
                    _reloadFlow.emit(Unit)
                }
        }
    }

    fun getTodayData(lat: String, lon: String) {
        weatherInteractor
            .getCurrentWeather(lat, lon)
            .doOnSubscribe { loaderLiveData.value = true }
            .doAfterTerminate { loaderLiveData.value = false }
            .subscribe({ currentWeather ->
                _todayLiveData.value = currentWeather
            }, { errorLiveData.value = it.message })
            .addToDisposable()
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