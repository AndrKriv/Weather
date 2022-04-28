package com.example.weather.extension

import com.example.weather.data.database.model.ForecastEntity
import com.example.weather.data.database.model.TodayEntity
import com.example.weather.domain.entity.ForecastInfo
import com.example.weather.domain.entity.TodayInfo
import com.example.weather.presentation.forecast.model.ForecastUIModel
import com.example.weather.presentation.today.model.TodayUIModel

fun ForecastInfo.toUIModel(): ForecastUIModel =
    ForecastUIModel(
        date = time.toDate(),
        description = weather.single().description,
        degrees = temp.degrees.toDouble()
    )

fun ForecastEntity.toUIModel(): ForecastUIModel =
    ForecastUIModel(
        date = date,
        description = description,
        degrees = degrees
    )

fun ForecastUIModel.toEntityModel(): ForecastEntity =
    ForecastEntity(
        date = date.toDate(),
        description = description,
        degrees = degrees
    )

fun ForecastInfo.toEntityModel(): ForecastEntity =
    ForecastEntity(
        date = time.toDate(),
        description = weather.single().description,
        degrees = temp.degrees.toDouble()
    )

fun List<ForecastEntity>.fromEntityToUIModelList(): List<ForecastUIModel> {

    val list = mutableListOf<ForecastUIModel>()
    for (value in this) {
        list.add(value.toUIModel())
    }
    return list
}

fun List<ForecastInfo>.fromInfoToUIModelList(): List<ForecastUIModel> {

    val list = mutableListOf<ForecastUIModel>()
    for (value in this) {
        list.add(value.toUIModel())
    }
    return list
}

fun TodayInfo.toUIModel(): TodayUIModel =
    TodayUIModel(
        city = this.city,
        date = this.date.toDate(),
        degrees = this.main.temp.toDouble(),
        description = this.weather.single().description,
        pressure = this.main.pressure.convertPressure(),
        humidity = this.main.humidity.toDouble(),
        wind = this.wind.deg,
        windSpeed = this.wind.speed.toDouble()
    )

fun TodayInfo.toEntity(): TodayEntity =
    TodayEntity(
        city = this.city,
        date = this.date,
        degrees = this.main.temp.toDouble(),
        description = this.weather.single().description,
        pressure = this.main.pressure.convertPressure(),
        humidity = this.main.humidity.toDouble(),
        wind = this.wind.deg,
        windSpeed = this.wind.speed.toDouble()
    )

fun TodayEntity.toUIModel(): TodayUIModel =
    TodayUIModel(
        city = city,
        date = date,
        degrees = degrees,
        description = description,
        pressure = pressure,
        humidity = humidity,
        wind = wind,
        windSpeed = windSpeed
    )