package com.example.weather.mvvm.presentation.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.mvvm.core.TodayInfo
import com.example.weather.mvvm.data.APIService
import com.example.weather.mvvm.data.ObservableRepository
import com.example.weather.mvvm.data.repository.LocationImpl
import com.example.weather.mvvm.domain.usecase.GetLocationUseCase
import com.example.weather.mvvm.domain.usecase.GetWeatherPictureUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TodayViewModel : ViewModel() {
    val todayInfo = MutableLiveData<TodayInfo>()
    private val disposable = CompositeDisposable()

    private val location by lazy { LocationImpl() }
    private val useCase by lazy { GetLocationUseCase(location) }
    private val image by lazy { GetWeatherPictureUseCase() }
    private val api = APIService

    fun getTodayData() {
            disposable.add(ObservableRepository(api.create()).todayData(
                useCase.execute().latitude.toString(),
                useCase.execute().longitude.toString()
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                           res -> todayInfo.value = res
                           },{

                    Log.e(
                        "AAA",
                        it.message.toString()
                    )
                })
            )
            Log.e(
                "AAA",
                useCase.execute().latitude.toString() + " + " + useCase.execute().longitude.toString()
            )
    }

    private fun send(messageText: String) = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, messageText)
        type = "text/plain"
    }

    fun sendInfoChooser(str: String): Intent {
        return Intent.createChooser(
            send(str), "Launch"
        )
    }

    fun loadImg():Int{
        return image.execute(todayInfo.value?.weather?.get(0)?.description.toString())
    }

    fun stringToShare(
        city: String,
        degrees: String,
        description: String,
        windSpeed: String,
        humidity: String,
        pressure: String
    ) =
        """
    В $city сейчас $degrees°C, на улице $description, скорость ветра $windSpeed м/с.
    Влажность $humidity%, давление $pressure мм рт. ст.
    """.trimIndent()

    override fun onCleared() {
        super.onCleared()
        Log.e("AAA", "vm cleared")
    }
}