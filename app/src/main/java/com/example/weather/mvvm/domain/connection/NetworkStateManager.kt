package com.example.weather.mvvm.domain.connection

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkStateManager {

    companion object {
        private var networkStateManager: NetworkStateManager? = null
        private val networkStatusLiveData = MutableLiveData<Boolean?>()

        fun getInstance(): NetworkStateManager? {
            if (networkStateManager == null) {
                networkStateManager = NetworkStateManager()
            }
            return networkStateManager
        }
    }

    fun setNetworkConnectivityStatus(connectivityStatus: Boolean) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            networkStatusLiveData.setValue(connectivityStatus)
        } else {
            networkStatusLiveData.postValue(connectivityStatus)
        }
    }

    fun getNetworkConnectivityStatus(): LiveData<Boolean?> = networkStatusLiveData
}