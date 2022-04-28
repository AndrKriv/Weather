package com.example.weather.di.module

import android.content.Context
import androidx.room.Room
import com.example.weather.core.Constants
import com.example.weather.data.database.WeatherDatabase
import com.example.weather.data.database.dao.ForecastDao
import com.example.weather.data.database.dao.TodayDao
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
    fun providesDatabase(context: Context): WeatherDatabase =
        Room
            .databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                Constants.DATABASE_NAME
            )
            .build()
}