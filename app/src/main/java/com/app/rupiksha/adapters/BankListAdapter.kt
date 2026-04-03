package com.app.rupiksha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.rupiksha.R
import com.app.rupiksha.databinding.BanklistLayoutBinding
import com.app.rupiksha.models.PayoutAccountModel

class BankListAdapter(
    private val context: Context,
    private val list: MutableList<PayoutAccountModel>,
    private val onItemClick: OnItemClick
) : RecyclerView.Adapter<BankListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: BanklistLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.banklist_layout,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder.binding.line2.visibility = View.VISIBLE
        } else {
            holder.binding.line2.visibility = View.GONE
        }
        
        val model = list[position]
        holder.binding.valueaccountnumber.text = model.account
        holder.binding.valuename.text = model.name
        holder.binding.valuestatus.text = model.status

        holder.binding.valueaction.setOnClickListener {
            onItemClick.onClick(position)
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(val binding: BanklistLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onClick(position: Int)
    }
}
