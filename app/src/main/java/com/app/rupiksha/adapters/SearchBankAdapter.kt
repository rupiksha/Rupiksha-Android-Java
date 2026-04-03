package com.app.rupiksha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.rupiksha.R
import com.app.rupiksha.databinding.OperatorRecycleviewListBinding
import com.app.rupiksha.models.BankModel

class SearchBankAdapter(
    private val context: Context,
    private val list: List<BankModel>,
    private val onItemClick: OnItemClick
) : RecyclerView.Adapter<SearchBankAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: OperatorRecycleviewListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.operator_recycleview_list,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.binding.title.text = model.name

        holder.binding.llRecharge.setOnClickListener {
            onItemClick.onClick(position)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(val binding: OperatorRecycleviewListBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onClick(position: Int)
    }
}
