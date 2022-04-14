package com.example.weather.room.dao

import androidx.room.*
import com.example.weather.room.model.ForecastEntity
import io.reactivex.Single

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(forecastEntity: List<ForecastEntity>)

    @Query("SELECT * FROM forecast_table")
    fun getForecastData(): Single<List<ForecastEntity>>

    @Query("SELECT count(id) FROM forecast_table")
    fun getTableSize(): Int

    @Delete
    fun removeData(forecastEntity: List<ForecastEntity>)
}