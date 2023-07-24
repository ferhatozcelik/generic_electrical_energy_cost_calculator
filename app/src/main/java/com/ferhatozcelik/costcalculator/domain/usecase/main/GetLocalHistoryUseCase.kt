package com.ferhatozcelik.costcalculator.domain.usecase.main

import android.util.Log
import com.ferhatozcelik.costcalculator.common.Resource
import com.ferhatozcelik.costcalculator.data.model.History
import com.ferhatozcelik.costcalculator.domain.repository.CostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetLocalHistoryUseCase @Inject constructor(private val costRepository: CostRepository) {
    private val TAG = GetLocalHistoryUseCase::class.java.simpleName
    operator fun invoke(serialNumber: String): Flow<Resource<List<History>>> = flow {
        emit(Resource.Loading)
        try {
            costRepository.getHistoryData().let { it ->
                val tempList = mutableListOf<History>()
                it.forEach {
                    if (serialNumber == it.serialNumber) {
                        tempList.add(History(it.serialNumber, it.units, it.cost))
                    }
                }
                emit(Resource.Success(tempList))
            }

        } catch (e: IOException) {
            Log.d(TAG, e.message.toString())
            emit(Resource.Error(e.localizedMessage.orEmpty()))
        }
    }
}