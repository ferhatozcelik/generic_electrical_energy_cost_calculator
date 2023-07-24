package com.ferhatozcelik.costcalculator.ui.fragments.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferhatozcelik.costcalculator.common.Resource
import com.ferhatozcelik.costcalculator.data.model.CostCalculation
import com.ferhatozcelik.costcalculator.data.model.Handle
import com.ferhatozcelik.costcalculator.data.model.History
import com.ferhatozcelik.costcalculator.domain.usecase.main.GetLocalHistoryUseCase
import com.ferhatozcelik.costcalculator.domain.usecase.main.SaveLocalHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLocalUseCase: GetLocalHistoryUseCase,
    private val saveLocalHistoryUseCase: SaveLocalHistoryUseCase,
    @SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) : ViewModel() {
    private val TAG = HomeViewModel::class.java.simpleName

    private var lastReading: History? = null

    val historyList: MutableLiveData<List<History>> = MutableLiveData(emptyList())
    val costCalculationList: MutableLiveData<List<CostCalculation>> = MutableLiveData(emptyList())

    var isProgressBarStatus: MutableLiveData<Boolean> = MutableLiveData(false)
    val totalCalculatedBill: MutableLiveData<Int> = MutableLiveData(0)

    private val tableList = listOf(
        mapOf("range" to 1..100, "rate" to 5),
        mapOf("range" to 101..500, "rate" to 8),
        mapOf("range" to 501..Int.MAX_VALUE, "rate" to 10)
    )

    fun requiredValidation(serialNumberTxt: String, currentReadingTxt: String): Handle {

        val serialNumberValidation: Handle = validateSerialNumber(serialNumberTxt)
        val currentReadingValidation: Handle = validateCurrentReading(currentReadingTxt)
        return if (!serialNumberValidation.isError) {
            if (!currentReadingValidation.isError) {
                Log.d(TAG, "Success Validation")
                Handle(false, "Success Validation")
            } else {
                Handle(true, currentReadingValidation.message)
            }
        } else {
            Handle(true, serialNumberValidation.message)
        }

    }

    fun validateCurrentReading(currentReadingTxt: String): Handle {
        if (currentReadingTxt.isNotEmpty()) {
            return try {
                val currentReadingValue: Int = currentReadingTxt.toInt()
                if (currentReadingValue > 0 && currentReadingValue < Int.MAX_VALUE) {
                    Log.d(TAG, "Success Validation Current Reading")
                    Handle(false, "Success Validation Current Reading")
                } else {
                    Log.d(TAG, "Must be a positive number")
                    Handle(true, "Must be a positive number")
                }
            } catch (e: NumberFormatException) {
                Log.e(TAG, e.message.toString())
                Handle(true, "Wrong Number format: " + e.message)
            }
        } else {
            Log.d(TAG, "Please CurrentReading Enter value")
            return Handle(true, "Please CurrentReading Enter value")
        }

    }

    fun validateSerialNumber(serialNumberTxt: String): Handle {
        return if (serialNumberTxt.isNotEmpty()) {
            try {
                val serialNumberValue = serialNumberTxt.toInt()
                if (serialNumberTxt.length < 10) {
                    Log.d(TAG, "Success Validation Serial Number")
                    Handle(false, "Success Validation Serial Number")
                } else {
                    Log.d(TAG, "Serial Number must be greater than 10")
                    Handle(true, "Serial Number must be greater than 10")
                }
            } catch (e: NumberFormatException) {
                Log.e(TAG, e.message.toString())
                Handle(true, "Wrong Number format: " + e.message)
            }
        } else {
            Log.d(TAG, "Please Serial Number Enter value")
            Handle(true, "Please Serial Number Enter value")
        }

    }

    fun BillCalculator(costCalculation: CostCalculation) {
        val unitForCalculation: Int = if ((lastReading?.units ?: 0) == 0) {
            costCalculation.value
        } else {
            costCalculation.value - (lastReading?.units ?: 0)
        }

        val index = tableList.indexOfFirst {
            val range = it["range"] as? ClosedRange<Int>
            range?.contains(unitForCalculation) ?: false
        }

        if (index != -1) {
            calculateBill(costCalculation.serialNumber, index, unitForCalculation)
        }

    }

    private fun calculateBill(serialNumber: String, index: Int, units: Int) {
        Log.d(TAG, "Calculate Bill")
        val bill: MutableList<CostCalculation> = mutableListOf()

        var breakdownOfUnits = 0
        var totalBill = 0

        for (i in 0..index) {
            val maxValue = (tableList[i]["range"] as? ClosedRange<Int>)?.endInclusive ?: 0
            val minValue = (tableList[i]["range"] as? ClosedRange<Int>)?.start ?: 0
            val rate = (tableList[i]["rate"] as? Int) ?: 0


            val canConsumedUnit = if (i != tableList.size - 1) {
                (maxValue - minValue) + 1
            } else {
                units - breakdownOfUnits
            }

            var unitToBeConsumed = 0
            unitToBeConsumed = when {
                canConsumedUnit > units -> units - breakdownOfUnits
                canConsumedUnit == units -> units
                canConsumedUnit > units - breakdownOfUnits -> units - breakdownOfUnits
                else -> canConsumedUnit
            }

            val cost = rate * unitToBeConsumed
            totalBill += cost
            bill.add(CostCalculation(serialNumber = "$unitToBeConsumed x $rate", value = cost))
            breakdownOfUnits += unitToBeConsumed
        }

        if (bill.isNotEmpty()) {
            bill.add(CostCalculation(serialNumber = "Total", value = totalBill))
        }

        Log.d(TAG, "Calculate Bill List: $bill")

        costCalculationList.postValue(bill)

        getHistory(serialNumber)
    }

    fun getHistory(serialNumber: String) = viewModelScope.launch {

        try {
            getLocalUseCase.invoke(serialNumber).collect { dataset ->
                dataset.let {
                    when (it) {
                        is Resource.Success<List<History>> -> {
                            isProgressBarStatus.postValue(true)
                            historyList.postValue(it.data)
                            Log.d(TAG, "Get History")
                        }

                        is Resource.Error -> {
                            Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                            isProgressBarStatus.postValue(false)
                            Log.d(TAG, "Get History Error: " + it.errorMessage)
                        }

                        is Resource.Loading -> {
                            isProgressBarStatus.postValue(false)
                            Log.d(TAG, "Get History Loading")
                        }
                    }
                }

            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }


    fun saveHistory(serialNumber: String, currentReading: String) = viewModelScope.launch {
        try {
            saveLocalHistoryUseCase.invoke(History(serialNumber, currentReading.toInt(), totalCalculatedBill.value!!))
            Log.d(TAG, "Saved History")
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

}