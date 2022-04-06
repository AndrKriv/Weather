package com.example.weather.di.component

import android.app.Application
import com.example.weather.di.module.*
import com.example.weather.mvvm.presentation.BottomActivity
import com.example.weather.mvvm.presentation.app.App
import com.example.weather.mvvm.presentation.fragments.ForecastFragment
import com.example.weather.mvvm.presentation.fragments.TodayFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
    ViewModelModule::class,
    NetworkModule::class,
    ViewModelFactoryModule::class,
    InternetMonitoringModule::class,
    DatabaseModule::class
]
)
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
    fun inject(app: App)
}