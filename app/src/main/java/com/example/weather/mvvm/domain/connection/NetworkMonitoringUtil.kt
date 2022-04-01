package com.example.weather.mvvm.domain.connection

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import javax.inject.Inject

class NetworkMonitoringUtil @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val networkStateManager: NetworkStateManager
) : NetworkCallback() {

    private val networkRequest: NetworkRequest =
        NetworkRequest
            .Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        networkStateManager.setNetworkConnectivityStatus(true)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        if(connectivityManager.activeNetworkInfo?.isConnected == true)
            Log.e("AAA","Если одновременно включались wi-fi и mobile, то прилетал false -> вывод, нужна эта проверка")
        else networkStateManager.setNetworkConnectivityStatus(false)
    }

    fun registerNetworkCallbackEvents() =
        connectivityManager.registerNetworkCallback(networkRequest, this)

    fun checkNetworkState() {
        try {
            val networkInfo = connectivityManager.activeNetworkInfo
            networkStateManager.setNetworkConnectivityStatus(
                networkInfo != null
                        && networkInfo.isConnected
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
