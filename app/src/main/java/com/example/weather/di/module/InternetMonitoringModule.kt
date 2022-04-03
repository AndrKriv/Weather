package com.example.weather.di.module

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.weather.mvvm.domain.connection.NetworkMonitoringUtil
import com.example.weather.mvvm.domain.connection.NetworkStateManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InternetMonitoringModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(application: Application): ConnectivityManager =
        try {
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        } catch (e: Exception) {
            throw RuntimeException("Error getting Connectivity Manager")
        }

    @Provides
    @Singleton
    fun provideStateManager(): NetworkStateManager = NetworkStateManager()

    @Provides
    @Singleton
    fun provideNetworkRequest(): NetworkRequest =
        NetworkRequest
            .Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

    @Provides
    @Singleton
    fun provideNetworkMonitoringUtil(
        connectivityManager: ConnectivityManager,
        networkStateManager: NetworkStateManager,
        networkRequest: NetworkRequest
    ): NetworkMonitoringUtil = NetworkMonitoringUtil(
        connectivityManager,
        networkStateManager,
        networkRequest
    )
}