package com.example.weather.mvvm.presentation.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.R
import com.example.weather.mvvm.core.TodayInfo
import com.example.weather.mvvm.data.APIService
import com.example.weather.mvvm.domain.interactor.WeatherInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TodayViewModel : ViewModel() {

    private val disposable = CompositeDisposable()
    private val interactor = WeatherInteractor(APIService.create())
    private val _todayLiveData = MutableLiveData<TodayInfo>()
    val todayLiveData: LiveData<TodayInfo> = _todayLiveData
    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData


    fun getTodayData(lat: String, lon: String) {
        disposable.add(
            interactor
                .getCurrentWeather(lat, lon)
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

    fun loadImg(string: String): Int {
        return when (string) {
            "ясно" -> R.drawable.sun
            "пасмурно" -> R.drawable.cloudy
            "облачно с прояснениями" -> R.drawable.cloud_sun
            "небольшая облачность" -> R.drawable.cloud
            "переменная облачность" -> R.drawable.cloud
            "сильный дождь" -> R.drawable.rain
            "дождь" -> R.drawable.rain
            "небольшой дождь" -> R.drawable.rain_small
            "проливной дождь" -> R.drawable.rain
            "небольшой снег" -> R.drawable.small_snow
            "снег" -> R.drawable.snowing
            "снег с дождем" -> R.drawable.snow_rain
            "туман" -> R.drawable.fog
            else -> R.drawable.unknown
        }
    }

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