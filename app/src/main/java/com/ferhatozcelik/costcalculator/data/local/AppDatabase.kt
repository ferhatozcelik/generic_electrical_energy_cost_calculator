package com.ferhatozcelik.costcalculator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ferhatozcelik.costcalculator.data.dao.HistoryDao
import com.ferhatozcelik.costcalculator.data.entity.HistoryEntity
import com.ferhatozcelik.costcalculator.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [HistoryEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getHistoryDao(): HistoryDao

    class Callback @Inject constructor(private val database: Provider<AppDatabase>,
                                       @ApplicationScope private val applicationScope: CoroutineScope) : RoomDatabase.Callback()
}