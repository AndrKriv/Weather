package com.example.weather.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather.room.dao.ForecastDao
import com.example.weather.room.dao.TodayDao
import com.example.weather.room.model.ForecastEntity
import com.example.weather.room.model.TodayEntity

@Database(
    entities = [TodayEntity::class, ForecastEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun todayDao(): TodayDao
    abstract fun forecastDao(): ForecastDao
}