package com.example.weather.utils

import com.example.weather.mvvm.core.ForecastInfo
import com.example.weather.mvvm.presentation.ForecastUIModel
import com.example.weather.room.model.ForecastEntity

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

fun ForecastUIModel.toEntityModel(id: Int): ForecastEntity =
    ForecastEntity(
        date = date,
        description = description,
        degrees = degrees,
        id = id
    )

fun List<ForecastEntity>.fromEntityToUIModelList(): List<ForecastUIModel> {

    val list = mutableListOf<ForecastUIModel>()
    for (value in this) {
        list.add(value.toUIModel())
    }
    return list
}

fun List<ForecastUIModel>.fromUIToEntityList(): List<ForecastEntity> {

    val list = mutableListOf<ForecastEntity>()
    for ((i, value) in this.withIndex()) {
        list.add(value.toEntityModel(i))
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