package com.example.weather.di.component

import android.app.Application
import com.example.weather.di.module.NetworkModule
import com.example.weather.di.module.ViewModelFactoryModule
import com.example.weather.di.module.ViewModelModule
import com.example.weather.mvvm.presentation.app.App
import com.example.weather.mvvm.presentation.fragments.ForecastFragment
import com.example.weather.mvvm.presentation.fragments.TodayFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, NetworkModule::class, ViewModelFactoryModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun baseUrl(@Named("baseUrl") baseUrl: String): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
    fun inject(todayFragment: TodayFragment)
    fun inject(forecastFragment: ForecastFragment)
}