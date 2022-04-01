package com.example.weather.mvvm.domain.connection

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkMonitoringUtil(
    private val connectivityManager: ConnectivityManager
) : NetworkCallback() {

    private val networkRequest: NetworkRequest =
        NetworkRequest
            .Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

    private val networkStateManager: NetworkStateManager = NetworkStateManager.getInstance()!!

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        networkStateManager.setNetworkConnectivityStatus(true)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        networkStateManager.setNetworkConnectivityStatus(false)
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
