package com.ferhatozcelik.costcalculator.data.repository

import com.ferhatozcelik.costcalculator.data.dao.HistoryDao
import com.ferhatozcelik.costcalculator.data.entity.HistoryEntity
import com.ferhatozcelik.costcalculator.domain.repository.CostRepository

class CostRepositoryImpl(private val historyDao: HistoryDao) : CostRepository {

    override suspend fun getHistoryData() = historyDao.getHistoryData()

    override suspend fun saveHistory(historyEntity: HistoryEntity) = historyDao.insert(historyEntity)

}