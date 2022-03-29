package com.example.weather.mvvm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private fun clearDisposables() {
        compositeDisposable.dispose()
    }

    protected fun Disposable.addToDisposable() {
        compositeDisposable.add(this)
    }

    override fun onCleared() {
        super.onCleared()
        clearDisposables()
    }
}