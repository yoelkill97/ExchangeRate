package com.yoelkill.exchangerate.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yoelkill.exchangerate.data.local.dao.ConversionHistoryDao
import com.yoelkill.exchangerate.data.local.entity.ConversionHistoryEntity


@Database(entities = [ConversionHistoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun conversionHistoryDao(): ConversionHistoryDao
}