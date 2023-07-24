package com.ferhatozcelik.costcalculator.ui.fragments.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.ferhatozcelik.costcalculator.data.model.CostCalculation
import com.ferhatozcelik.costcalculator.data.model.History
import com.ferhatozcelik.costcalculator.databinding.FragmentHomeBinding
import com.ferhatozcelik.costcalculator.interfaces.ItemClickListener
import com.ferhatozcelik.costcalculator.ui.adapters.CostCalculationAdapter
import com.ferhatozcelik.costcalculator.ui.adapters.HistoryAdapter
import com.ferhatozcelik.costcalculator.ui.base.BaseFragment
import com.ferhatozcelik.costcalculator.util.ProgressDialog
import com.ferhatozcelik.costcalculator.util.gone
import com.ferhatozcelik.costcalculator.util.show
import com.ferhatozcelik.costcalculator.util.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {
    private val TAG = HomeFragment::class.java.simpleName

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "Called onViewCreated")

        binding.apply {
            binding.historyViewRoot.root.gone()
            binding.calculationViewRoot.root.gone()

            calculationViewRoot.buttonSave.setOnClickListener {
                val serialNumber: String = edittextSerialNumber.text.toString()
                val currentReading: String = edittextCurrentReading.text.toString()
                val verifyResult = viewModel.requiredValidation(serialNumber, currentReading)
                if (!verifyResult.isError) {
                    Log.d(TAG, "Called saveHistory")
                    viewModel.saveHistory(serialNumber, currentReading)
                }

            }

            buttonSubmit.setOnClickListener {
                val serialNumber: String = edittextSerialNumber.text.toString()
                val currentReading: String = edittextCurrentReading.text.toString()
                val verifyResult = viewModel.requiredValidation(serialNumber, currentReading)
                if (verifyResult.isError) {
                    context?.toast(verifyResult.message)
                } else {
                    Log.d(TAG, "Called BillCalculator")
                    viewModel.BillCalculator(CostCalculation(serialNumber, currentReading.toInt()))
                }
            }

            edittextSerialNumber.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    val serialNumber: String = edittextSerialNumber.text.toString()
                    val verifyResult = viewModel.validateSerialNumber(serialNumber)
                    if (!verifyResult.isError) {
                        Log.d(TAG, "Called getHistory")
                        viewModel.getHistory(serialNumber)
                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }
            })

        }

        HistoryList()

        CostCalculationList()

    }

    private fun HistoryList() {
        Log.d(TAG, "Called HistoryList")
        viewModel.historyList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.historyViewRoot.root.show()
                val adapter = HistoryAdapter(it, historyItemClickListener)
                binding.historyViewRoot.recyclerviewHistoryList.adapter = adapter
            } else {
               binding.historyViewRoot.root.gone()
            }
        }
    }

    private fun CostCalculationList() {
        Log.d(TAG, "Called CostCalculationList")
        viewModel.costCalculationList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.calculationViewRoot.root.show()
                val adapter = CostCalculationAdapter(it, costItemClickListener)
                binding.calculationViewRoot.recyclerviewCalculateList.adapter = adapter
            } else {
                binding.calculationViewRoot.root.gone()
            }
        }
    }

    private val historyItemClickListener = object : ItemClickListener {
        override fun onClick(objects: Any?) {
            val item = objects as History
        }
    }
    private val costItemClickListener = object : ItemClickListener {
        override fun onClick(objects: Any?) {
            val item = objects as CostCalculation
        }
    }

}