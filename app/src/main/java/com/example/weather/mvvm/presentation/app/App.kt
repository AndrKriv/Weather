package com.example.weather.mvvm.presentation.app

import android.app.Application
import com.example.weather.di.component.AppComponent
import com.example.weather.di.component.DaggerAppComponent
import com.example.weather.utils.Constants

class App : Application() {
    lateinit var daggerAppComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        daggerAppComponent = DaggerAppComponent
            .builder()
            .application(this)
            .baseUrl(Constants.BASE_URL)
            .build()
    }
}
