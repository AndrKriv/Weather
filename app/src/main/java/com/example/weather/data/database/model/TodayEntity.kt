package com.example.weather.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weather.core.Constants

@Entity(tableName = Constants.TODAY_TABLE_NAME)
data class TodayEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "city_name")
    val city: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "degrees")
    val degrees: Double,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "pressure")
    val pressure: Int,
    @ColumnInfo(name = "humidity")
    val humidity: Double,
    @ColumnInfo(name = "wind")
    val wind: Int,
    @ColumnInfo(name = "wind_speed")
    val windSpeed: Double
)