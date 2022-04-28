package com.example.weather.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.data.database.model.TodayEntity
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