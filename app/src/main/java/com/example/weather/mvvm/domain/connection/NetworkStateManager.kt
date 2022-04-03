package com.example.weather.mvvm.domain.connection

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import javax.inject.Inject

class NetworkStateManager @Inject constructor(){

    private val _networkStatusLiveData = MutableLiveData<Boolean>()
    val networkStatusLiveData: LiveData<Boolean> = _networkStatusLiveData.distinctUntilChanged()

    fun setNetworkConnectivityStatus(connectivityStatus: Boolean) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            _networkStatusLiveData.setValue(connectivityStatus)
        } else {
            _networkStatusLiveData.postValue(connectivityStatus)
        }
    }
}