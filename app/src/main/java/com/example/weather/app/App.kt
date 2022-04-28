package com.example.weather.app

import android.app.Application
import com.example.weather.core.Constants
import com.example.weather.core.connection.NetworkMonitoringUtil
import com.example.weather.di.AppComponent
import com.example.weather.di.DaggerAppComponent
import javax.inject.Inject

class App : Application() {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var networkMonitoringUtil: NetworkMonitoringUtil

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
            .builder()
            .application(this)
            .baseUrl(Constants.BASE_URL)
            .build()
            .also { appComponent = it }
            .inject(this)
    }
}