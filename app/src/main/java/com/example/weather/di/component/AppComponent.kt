package com.example.weather.di.component

import android.app.Application
import com.example.weather.di.module.ConnectionModule
import com.example.weather.di.module.NetworkModule
import com.example.weather.di.module.ViewModelFactoryModule
import com.example.weather.di.module.ViewModelModule
import com.example.weather.mvvm.presentation.BottomActivity
import com.example.weather.mvvm.presentation.app.App
import com.example.weather.mvvm.presentation.fragments.ForecastFragment
import com.example.weather.mvvm.presentation.fragments.TodayFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, NetworkModule::class, ViewModelFactoryModule::class, ConnectionModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun baseUrl(baseUrl: String): Builder

        fun build(): AppComponent
    }

    fun inject(todayFragment: TodayFragment)
    fun inject(forecastFragment: ForecastFragment)
    fun inject(bottomActivity: BottomActivity)
}