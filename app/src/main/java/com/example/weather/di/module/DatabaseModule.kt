package com.example.weather.di.module

import android.app.Application
import androidx.room.Room
import com.example.weather.room.dao.WeatherDao
import com.example.weather.room.database.ForecastDatabase
import com.example.weather.utils.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDao(database: ForecastDatabase): WeatherDao =
        database.weatherDao()

    @Singleton
    @Provides
    fun providesDatabase(application: Application): ForecastDatabase =
        Room
            .databaseBuilder(
                application.applicationContext,
                ForecastDatabase::class.java,
                Constants.DATABASE_NAME
            )
            .build()
}
