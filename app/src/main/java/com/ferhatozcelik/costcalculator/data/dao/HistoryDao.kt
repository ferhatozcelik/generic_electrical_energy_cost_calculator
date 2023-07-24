package com.ferhatozcelik.costcalculator.data.dao

import androidx.room.*
import com.ferhatozcelik.costcalculator.data.entity.HistoryEntity

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history_table")
    suspend fun getHistoryData(): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyEntity: HistoryEntity)

    @Update
    suspend fun update(historyEntity: HistoryEntity)

    @Delete
    suspend fun delete(historyEntity: HistoryEntity)

}