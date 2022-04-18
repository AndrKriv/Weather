package com.example.weather.mvvm.presentation.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.mvvm.core.TodayInfo
import com.example.weather.mvvm.domain.interactor.WeatherInteractor
import com.example.weather.utils.SingleLiveEvent
import javax.inject.Inject

class TodayViewModel @Inject constructor(
    private val interactor: WeatherInteractor
) : BaseViewModel() {

    private val _todayLiveData = MutableLiveData<TodayInfo>()
    val todayLiveData: LiveData<TodayInfo> = _todayLiveData
    val errorLiveData = SingleLiveEvent<String>()

    fun getTodayData(lat: String, lon: String) {
        interactor
            .getCurrentWeather(lat, lon)
            .subscribe({ currentWeather ->
                _todayLiveData.value = currentWeather
            }, {
                errorLiveData.value = it.message
            })
            .addToDisposable()
    }

    fun sendInfoChooser(messageText: String): Intent = Intent.createChooser(
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
        pressure: Double
    ) =
        """
    В $city сейчас $degrees°C, на улице $description, скорость ветра $windSpeed м/с.
    Влажность $humidity%, давление $pressure мм рт. ст.
    """.trimIndent()
}