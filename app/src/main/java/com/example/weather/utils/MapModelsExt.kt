package com.example.weather.utils

import androidx.lifecycle.LiveData
import com.example.weather.mvvm.core.ForecastInfo
import com.example.weather.mvvm.domain.connection.NetworkStateManager
import com.example.weather.mvvm.presentation.ForecastUIModel
import com.example.weather.room.model.ForecastEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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

fun List<ForecastInfo>.fromInfoToEntityList(): List<ForecastEntity> {

    val list = mutableListOf<ForecastEntity>()
    for (value in this) {
        list.add(value.toEntityModel())
    }
    return list
}