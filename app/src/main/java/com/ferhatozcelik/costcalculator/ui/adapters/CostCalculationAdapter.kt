package com.ferhatozcelik.costcalculator.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ferhatozcelik.costcalculator.data.model.CostCalculation
import com.ferhatozcelik.costcalculator.databinding.CostCalculateItemBinding
import com.ferhatozcelik.costcalculator.interfaces.ItemClickListener

class CostCalculationAdapter(var list: List<CostCalculation>, var itemClickListener: ItemClickListener) : RecyclerView.Adapter<CostCalculationAdapter.CostCalculationViewHolder>() {
    private val TAG = CostCalculationAdapter::class.java.simpleName
    lateinit var itemGoalsPreviewBinding: CostCalculateItemBinding

    class CostCalculationViewHolder(binding: CostCalculateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: CostCalculateItemBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CostCalculationViewHolder {
        val binding = CostCalculateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CostCalculationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CostCalculationViewHolder, position: Int) {
        val item = list[position]

        itemGoalsPreviewBinding = holder.binding

        holder.binding.apply {
            textViewUtilsValue.text = item.serialNumber
            textViewCostValue.text = item.value.toString()
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(item)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}
