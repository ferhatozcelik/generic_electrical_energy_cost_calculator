package com.ferhatozcelik.costcalculator.di

import com.ferhatozcelik.costcalculator.data.dao.HistoryDao
import com.ferhatozcelik.costcalculator.data.repository.CostRepositoryImpl
import com.ferhatozcelik.costcalculator.domain.repository.CostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideExampleRepository(historyDao: HistoryDao): CostRepository {
        return CostRepositoryImpl(historyDao)
    }

}