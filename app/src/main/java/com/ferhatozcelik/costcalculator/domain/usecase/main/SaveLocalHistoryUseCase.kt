package com.ferhatozcelik.costcalculator.domain.usecase.main

import android.util.Log
import com.ferhatozcelik.costcalculator.data.entity.HistoryEntity
import com.ferhatozcelik.costcalculator.data.model.History
import com.ferhatozcelik.costcalculator.domain.repository.CostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class SaveLocalHistoryUseCase @Inject constructor(private val costRepository: CostRepository) {
    private val TAG = SaveLocalHistoryUseCase::class.java.simpleName
    operator fun invoke(historyModel: History): Flow<Unit> = flow {
        try {
            costRepository.saveHistory(HistoryEntity(serialNumber = historyModel.serialNumber, units = historyModel.units, cost = historyModel.cost, createAt = System.currentTimeMillis()))
        } catch (e: IOException) {
            Log.d(TAG, e.message.toString())
        }
    }
}