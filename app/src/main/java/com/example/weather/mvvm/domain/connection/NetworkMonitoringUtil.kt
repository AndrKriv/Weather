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
    private val networkStateManager: NetworkStateManager,
    private val networkRequest: NetworkRequest
) : NetworkCallback() {

    init {
        networkStateManager.setNetworkConnectivityStatus(isNetworkAvailable())
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        networkStateManager.setNetworkConnectivityStatus(true)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        if (!isNetworkAvailable()) networkStateManager.setNetworkConnectivityStatus(false)
    }

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}
