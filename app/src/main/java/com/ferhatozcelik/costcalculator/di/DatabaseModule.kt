package com.ferhatozcelik.costcalculator.di

import android.app.Application
import androidx.room.Room
import com.ferhatozcelik.costcalculator.data.dao.HistoryDao
import com.ferhatozcelik.costcalculator.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: AppDatabase.Callback): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "local_database").fallbackToDestructiveMigration().addCallback(callback).build()
    }

    @Provides
    fun provideHistoryDao(database: AppDatabase): HistoryDao {
        return database.getHistoryDao()
    }
}