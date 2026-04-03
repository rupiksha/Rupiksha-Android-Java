package com.app.rupiksha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.rupiksha.R
import com.app.rupiksha.databinding.AllReportRecycleviewListBinding
import com.app.rupiksha.models.ReportTypeModel

class AllReportAdapter(
    private val context: Context,
    private val list: List<ReportTypeModel>,
    private val onItemClick: OnItemClick
) : RecyclerView.Adapter<AllReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: AllReportRecycleviewListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.all_report_recycleview_list,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.binding.title.text = model.name

        when (model.type) {
            "aeps" -> holder.binding.icon.setImageResource(R.drawable.aadhar_pay)
            "money-transfer" -> holder.binding.icon.setImageResource(R.drawable.dmt)
            "qtransfer" -> holder.binding.icon.setImageResource(R.drawable.q_transfar)
            "payout" -> holder.binding.icon.setImageResource(R.drawable.payout)
            "bbps" -> holder.binding.icon.setImageResource(R.drawable.bbps)
            "recharge" -> holder.binding.icon.setImageResource(R.drawable.mobile_recharge)
            "uti-coupon" -> holder.binding.icon.setImageResource(R.drawable.uti)
            "wallet" -> holder.binding.icon.setImageResource(R.drawable.wallet_new)
            "wallet-to-wallet" -> holder.binding.icon.setImageResource(R.drawable.wallet_to_wallet)
        }

        holder.binding.materialcard.setOnClickListener {
            onItemClick.onClick(position)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(val binding: AllReportRecycleviewListBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onClick(position: Int)
    }
}
