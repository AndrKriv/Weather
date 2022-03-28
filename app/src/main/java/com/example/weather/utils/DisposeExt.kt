package com.example.weather.utils

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


fun ViewModel.addDisposable(dispose: Disposable){
    CompositeDisposable().add(dispose)
}

fun ViewModel.removeDisposables() {
    CompositeDisposable().dispose()
}