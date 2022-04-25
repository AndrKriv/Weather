package com.example.weather.di.module

import android.app.Application
import androidx.room.Room
import com.example.weather.room.dao.ForecastDao
import com.example.weather.room.dao.TodayDao
import com.example.weather.room.database.WeatherDatabase
import com.example.weather.utils.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideTodayDao(database: WeatherDatabase): TodayDao = database.todayDao()

    @Provides
    @Singleton
    fun provideForecastDao(database: WeatherDatabase): ForecastDao = database.forecastDao()

    @Singleton
    @Provides
    fun providesDatabase(application: Application): WeatherDatabase =
        Room
            .databaseBuilder(
                application.applicationContext,
                WeatherDatabase::class.java,
                Constants.DATABASE_NAME
            )
            .build()
}