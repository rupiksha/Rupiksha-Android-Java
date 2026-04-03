package com.app.rupiksha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.rupiksha.R
import com.app.rupiksha.databinding.QtAccountLayoutBinding
import com.app.rupiksha.models.QtAccountModel

class QTAccountAdapter(
    private val context: Context,
    private val list: List<QtAccountModel>,
    private val onItemClick: OnItemClick
) : RecyclerView.Adapter<QTAccountAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: QtAccountLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.qt_account_layout,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.binding.bank.text = model.bname
        holder.binding.account.text = model.account
        holder.binding.txIfsc.text = model.ifsc
        holder.binding.name.text = model.name

        holder.binding.materialcard.setOnClickListener {
            onItemClick.onClick(position)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(val binding: QtAccountLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onClick(position: Int)
    }
}
