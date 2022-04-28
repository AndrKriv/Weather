package com.example.weather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather.data.database.dao.ForecastDao
import com.example.weather.data.database.dao.TodayDao
import com.example.weather.data.database.model.ForecastEntity
import com.example.weather.data.database.model.TodayEntity

@Database(
    entities = [TodayEntity::class, ForecastEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun todayDao(): TodayDao
    abstract fun forecastDao(): ForecastDao
}