package com.example.weather.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.data.database.model.ForecastEntity

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecastData(forecastEntity: List<ForecastEntity>)

    @Query("SELECT * FROM forecast_table")
    suspend fun getForecastData(): List<ForecastEntity>

    @Query("DELETE FROM forecast_table")
    fun removeForecastData()
}