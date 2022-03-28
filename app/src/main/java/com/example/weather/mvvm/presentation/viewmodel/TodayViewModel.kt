package com.example.weather.mvvm.presentation.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.mvvm.core.TodayInfo
import com.example.weather.mvvm.domain.interactor.WeatherInteractor
import com.example.weather.utils.addDisposable
import com.example.weather.utils.removeDisposables
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TodayViewModel @Inject constructor(private val interactor: WeatherInteractor) : ViewModel() {

    private val _todayLiveData = MutableLiveData<TodayInfo>()
    val todayLiveData: LiveData<TodayInfo> = _todayLiveData
    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun getTodayData(lat: String, lon: String) {
        addDisposable(
            interactor.getCurrentWeather(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ currentWeather ->
                    _todayLiveData.value = currentWeather
                }, {
                    _errorLiveData.value = it.message
                })
        )
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

    override fun onCleared() {
        super.onCleared()
        removeDisposables()
    }
}