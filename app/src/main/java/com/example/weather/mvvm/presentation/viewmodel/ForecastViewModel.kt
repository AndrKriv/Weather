package com.example.weather.mvvm.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.mvvm.presentation.UIModel
import com.example.weather.utils.fromForecastInfoToForecastDatabaseModel
import com.example.weather.utils.fromListForecastDatabaseToUIModel
import com.example.weather.mvvm.core.ForecastInfo
import com.example.weather.mvvm.domain.interactor.DatabaseInteractor
import com.example.weather.mvvm.domain.interactor.WeatherInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val interactor: WeatherInteractor,
    private val databaseInteractor: DatabaseInteractor
) : BaseViewModel() {

    private val _forecastLiveData = MutableLiveData<List<ForecastInfo>>()
    val forecastLiveData: LiveData<List<ForecastInfo>> = _forecastLiveData
    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData
    private val _databaseLiveData = MutableLiveData<List<UIModel>>()
    val databaseLiveData: LiveData<List<UIModel>> = _databaseLiveData

    fun getForecastData(lat: String, lon: String) {
        interactor
            .getForecastWeather(lat, lon)
            .map {
                it.list
            }
            .doAfterSuccess {
                for ((id, value) in it.withIndex()) {
                    databaseInteractor.insertData(
                        value.fromForecastInfoToForecastDatabaseModel(id)
                    )
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ forecastWeather ->
                _forecastLiveData.value = forecastWeather
            }, {
                databaseInteractor
                    .getForecastWeather()
                    .map {
                        it.fromListForecastDatabaseToUIModel()
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ forecastWeather ->
                        _databaseLiveData.value = forecastWeather
                    }, {
                        _errorLiveData.value = it.message
                    }).addToDisposable()
            })
            .addToDisposable()
    }
}