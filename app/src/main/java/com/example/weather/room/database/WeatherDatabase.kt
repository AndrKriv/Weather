package com.example.weather.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather.room.dao.WeatherDao
import com.example.weather.room.model.ForecastDatabaseModel

@Database(
    entities = [ForecastDatabaseModel::class],
    version = 1,
    exportSchema = false
)
abstract class ForecastDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}