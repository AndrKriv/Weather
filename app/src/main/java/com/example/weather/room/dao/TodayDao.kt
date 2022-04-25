package com.example.weather.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.room.model.TodayEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface TodayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodayData(todayEntity: TodayEntity)//: Completable

    @Query("SELECT * FROM today_table")
    fun getTodayData(): Single<TodayEntity>

    @Query("DELETE FROM today_table")
    fun removeTodayData()//: Completable
}