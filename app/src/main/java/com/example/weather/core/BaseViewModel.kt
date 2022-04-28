package com.example.weather.core

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    protected fun Disposable.addToDisposable() {
        compositeDisposable.add(this)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}