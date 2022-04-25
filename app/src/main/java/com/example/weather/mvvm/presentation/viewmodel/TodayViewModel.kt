package com.example.weather.mvvm.presentation.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.mvvm.domain.interactor.WeatherInteractor
import com.example.weather.mvvm.presentation.TodayUIModel
import com.example.weather.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class TodayViewModel @Inject constructor(
    private val interactor: WeatherInteractor
) : BaseViewModel() {

    private val _todayLiveData = MutableLiveData<TodayUIModel>()
    val todayLiveData: LiveData<TodayUIModel> = _todayLiveData
    val errorLiveData = SingleLiveEvent<String>()
    val loaderLiveData = SingleLiveEvent<Boolean>()
    val reloadLiveData: SingleLiveEvent<Unit> = SingleLiveEvent()

    init {
        interactor
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

    fun getTodayData(lat: String, lon: String) {
        interactor
            .getCurrentWeather(lat, lon)
            .doOnSubscribe {
                loaderLiveData.value = true
            }
            .doAfterTerminate {
                loaderLiveData.value = false
            }
            .subscribe({ currentWeather ->
                _todayLiveData.value = currentWeather
            }, {
                errorLiveData.value = it.message
            })
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