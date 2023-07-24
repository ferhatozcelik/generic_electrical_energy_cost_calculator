package com.ferhatozcelik.costcalculator.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ferhatozcelik.costcalculator.data.model.History
import com.ferhatozcelik.costcalculator.databinding.HistoryItemBinding
import com.ferhatozcelik.costcalculator.interfaces.ItemClickListener

class HistoryAdapter(var list: List<History>, var itemClickListener: ItemClickListener) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private val TAG = HistoryAdapter::class.java.simpleName
    lateinit var itemGoalsPreviewBinding: HistoryItemBinding

    class HistoryViewHolder(binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: HistoryItemBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = list[position]

        itemGoalsPreviewBinding = holder.binding

        holder.binding.apply {
            textViewUtilsValue.text = item.units.toString()
            textViewCostValue.text = item.cost.toString()
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(item)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}
