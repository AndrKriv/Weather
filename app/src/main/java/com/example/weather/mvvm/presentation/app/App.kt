package com.example.weather.mvvm.presentation.app

import android.app.Application
import android.net.ConnectivityManager
import com.example.weather.di.component.AppComponent
import com.example.weather.di.component.DaggerAppComponent
import com.example.weather.mvvm.domain.connection.NetworkMonitoringUtil
import com.example.weather.mvvm.domain.connection.NetworkStateManager
import com.example.weather.utils.Constants
import javax.inject.Inject

class App : Application() {

    var networkMonitoringUtil: NetworkMonitoringUtil? = null
    lateinit var appComponent: AppComponent

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    @Inject
    lateinit var networkStateManager: NetworkStateManager

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
            .builder()
            .application(this)
            .baseUrl(Constants.BASE_URL)
            .build()
            .also { appComponent = it }.inject(this)

        networkMonitoringUtil = NetworkMonitoringUtil(connectivityManager, networkStateManager)
        networkMonitoringUtil?.checkNetworkState()
        networkMonitoringUtil?.registerNetworkCallbackEvents()
    }
}