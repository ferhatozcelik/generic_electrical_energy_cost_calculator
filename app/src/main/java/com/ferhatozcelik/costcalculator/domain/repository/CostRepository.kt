package com.ferhatozcelik.costcalculator.domain.repository

import com.ferhatozcelik.costcalculator.data.entity.HistoryEntity

interface CostRepository {

    suspend fun getHistoryData(): List<HistoryEntity>

    suspend fun saveHistory(historyEntity: HistoryEntity)

}