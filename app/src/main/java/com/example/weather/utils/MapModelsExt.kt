package com.example.weather.utils

import com.example.weather.mvvm.core.ForecastInfo
import com.example.weather.mvvm.presentation.UIModel
import com.example.weather.room.model.ForecastDatabaseModel

fun ForecastInfo.fromForecastInfoToForecastDatabaseModel(id: Int): ForecastDatabaseModel =
    ForecastDatabaseModel(
        date = time.toDate(),
        description = weather[0].description,
        degrees = temp.degrees.toDouble(),
        id = id
    )

fun ForecastInfo.fromForecastDatabaseToUIModel(): UIModel =
    UIModel(
        date = time.toDate(),
        description = weather[0].description,
        degrees = temp.degrees.toDouble()
    )

fun ForecastDatabaseModel.fromForecastDatabaseToUIModel(): UIModel =
    UIModel(
        date = date,
        description = description,
        degrees = degrees
    )

fun List<ForecastDatabaseModel>.fromListForecastDatabaseToUIModel(): List<UIModel> {

    val list = mutableListOf<UIModel>()
    for (value in this) {
        list.add(value.fromForecastDatabaseToUIModel())
    }
    return list
}