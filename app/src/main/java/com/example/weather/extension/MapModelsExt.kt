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
        city = city,
        date = date,
        degrees = main.temp.toDouble(),
        description = weather.single().description,
        pressure = main.pressure.convertPressure(),
        humidity = main.humidity.toDouble(),
        wind = wind.deg,
        windSpeed = wind.speed.toDouble()
    )

fun TodayInfo.toEntity(): TodayEntity =
    TodayEntity(
        city = city,
        date = date,
        degrees = main.temp.toDouble(),
        description = weather.single().description,
        pressure = main.pressure.convertPressure(),
        humidity = main.humidity.toDouble(),
        wind = wind.deg,
        windSpeed = wind.speed.toDouble()
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