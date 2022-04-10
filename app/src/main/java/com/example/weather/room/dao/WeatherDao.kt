package com.example.weather.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.room.model.ForecastEntity
import io.reactivex.Single

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(forecastEntity: ForecastEntity)

    @Query("SELECT * FROM forecast_table")
    fun getForecastData(): Single<List<ForecastEntity>>
}