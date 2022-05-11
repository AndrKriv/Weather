package com.example.weather.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.data.database.model.TodayEntity

@Dao
interface TodayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodayData(todayEntity: TodayEntity)

    @Query("SELECT * FROM today_table")
    suspend fun getTodayData(): TodayEntity

    @Query("DELETE FROM today_table")
    fun removeTodayData()
}