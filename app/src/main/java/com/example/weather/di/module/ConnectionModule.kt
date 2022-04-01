package com.example.weather.di.module

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.example.weather.mvvm.domain.connection.NetworkStateManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ConnectionModule {

    @Provides
    @Singleton
    fun connectivityManager(application: Application): ConnectivityManager =
        application.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

    @Provides
    @Singleton
    fun stateManager(): NetworkStateManager = NetworkStateManager()
}