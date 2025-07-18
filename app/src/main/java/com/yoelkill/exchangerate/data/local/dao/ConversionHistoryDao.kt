package com.yoelkill.exchangerate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yoelkill.exchangerate.data.local.entity.ConversionHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversionHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ConversionHistoryEntity)

    @Query("SELECT * FROM conversion_history ORDER BY timestamp DESC")
    fun getHistory(): Flow<List<ConversionHistoryEntity>>


    @Query("DELETE FROM conversion_history")
    suspend fun clearHistory()
}
