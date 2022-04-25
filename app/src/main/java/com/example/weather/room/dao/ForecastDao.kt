package com.example.weather.room.dao

import androidx.room.*
import com.example.weather.room.model.ForecastEntity
import io.reactivex.Single

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecastData(forecastEntity: List<ForecastEntity>)

    @Query("SELECT * FROM forecast_table")
    fun getForecastData(): Single<List<ForecastEntity>>

    @Query("DELETE FROM forecast_table")
    fun removeForecastData()
}