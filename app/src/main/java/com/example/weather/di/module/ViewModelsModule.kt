package com.example.weather.di.module

import androidx.lifecycle.ViewModel
import com.example.weather.di.ViewModelKey
import com.example.weather.presentation.forecast.ForecastViewModel
import com.example.weather.presentation.today.TodayViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @IntoMap
    @Binds
    @ViewModelKey(TodayViewModel::class)
    abstract fun bindTodayViewModel(todayViewModel: TodayViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ForecastViewModel::class)
    abstract fun bindForecastViewModel(forecastViewModel: ForecastViewModel): ViewModel
}