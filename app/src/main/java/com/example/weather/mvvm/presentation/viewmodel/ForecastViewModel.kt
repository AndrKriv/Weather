package com.example.weather.mvvm.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.mvvm.core.ForecastList
import com.example.weather.mvvm.data.APIService
import com.example.weather.mvvm.data.ObservableRepository
import com.example.weather.mvvm.data.repository.LocationImpl
import com.example.weather.mvvm.domain.usecase.GetLocationUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ForecastViewModel : ViewModel() {
    private val disposable = CompositeDisposable()
    private val api=APIService
    private val location by lazy { LocationImpl() }
    private val useCase by lazy { GetLocationUseCase(location) }
    var liveData = MutableLiveData<ForecastList>()

    fun getForecastData() {
            disposable.add(
                ObservableRepository(api.create()).forecastData(
                useCase.execute().latitude.toString(),
                useCase.execute().longitude.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    res->liveData.value=res
                },{
                    Log.e("AAAA", "Error")
                })
            )
        Log.e("AAAA", useCase.execute().latitude.toString() +" + "+useCase.execute().longitude.toString())

    }
}