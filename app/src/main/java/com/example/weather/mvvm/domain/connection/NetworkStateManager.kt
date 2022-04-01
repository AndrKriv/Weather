package com.example.weather.mvvm.domain.connection

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkStateManager {

    private val networkStatusLiveData = MutableLiveData<Boolean?>()

    fun setNetworkConnectivityStatus(connectivityStatus: Boolean) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            networkStatusLiveData.setValue(connectivityStatus)
        } else {
            networkStatusLiveData.postValue(connectivityStatus)
        }
    }

    fun getNetworkConnectivityStatus(): LiveData<Boolean?> = networkStatusLiveData
}