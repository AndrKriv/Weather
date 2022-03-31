package com.example.weather.mvvm.domain.usecase

import android.content.Context
import android.net.*
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class InternetConnectionReader(
    val context: Context,
    private val connectivityManager: ConnectivityManager
) : ConnectivityManager.NetworkCallback() {

    private val liveData = object : MutableLiveData<Boolean>() {
        override fun onActive() {
            super.onActive()
            register()
        }

        override fun onInactive() {
            super.onInactive()
            unregister()
        }
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        setInternetStatus(true)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        setInternetStatus(false)
    }

    private fun setInternetStatus(isConnected: Boolean) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            liveData.value = isConnected
        } else {
            liveData.postValue(isConnected)
        }
    }

    fun register() {
        connectivityManager.registerNetworkCallback(
            NetworkRequest
                .Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .build(),
            this
        )
        Log.e("AAA", "reg")
    }

    fun unregister() {
        connectivityManager.unregisterNetworkCallback(this)
        Log.e("AAA", "unreg")
    }

    fun getLiveData(): LiveData<Boolean> = liveData

    fun checkNetworkState() {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (networkInfo == null || !networkInfo.isConnected)
            liveData.value = false
    }
}