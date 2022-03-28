package com.example.weather.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.di.key.ViewModelKey
import com.example.weather.mvvm.presentation.factory.ViewModelFactory
import com.example.weather.mvvm.presentation.viewmodel.ForecastViewModel
import com.example.weather.mvvm.presentation.viewmodel.TodayViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(TodayViewModel::class)
    abstract fun bindTodayViewModel(todayViewModel: TodayViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ForecastViewModel::class)
    abstract fun bindForecastViewModel(forecastViewModel: ForecastViewModel): ViewModel

    @Binds
    abstract fun provideViewModelFactory(viewModelsFactory: ViewModelFactory): ViewModelProvider.Factory
}