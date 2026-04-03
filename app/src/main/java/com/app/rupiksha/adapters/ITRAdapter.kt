package com.app.rupiksha.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.rupiksha.R
import com.app.rupiksha.databinding.RowInvestmentDetailBinding
import com.app.rupiksha.extra.CustomToastNotification
import com.app.rupiksha.interfaces.ItemClickListener
import com.app.rupiksha.models.TaxFormDetailModel

class ITRAdapter(
    private val context: Context,
    private val itrList: List<TaxFormDetailModel>,
    private val clickListener: ItemClickListener
) : RecyclerView.Adapter<ITRAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowInvestmentDetailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_investment_detail,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = itrList[position]

        if (!model.imageName.isNullOrEmpty()) {
            holder.binding.txtNoFileChoose.text = model.imageName
        } else {
            holder.binding.txtNoFileChoose.text = context.resources.getString(R.string.txt_no_file_chosen)
        }

        holder.binding.txtTitle.setText(model.title ?: "")
        holder.binding.txtAmount.setText(model.amount ?: "")

        holder.binding.cardViewChose.setOnClickListener {
            model.title = holder.binding.txtTitle.text.toString()
            model.amount = holder.binding.txtAmount.text.toString()
            clickListener.itemClick(model, position, "AddImage")
        }

        holder.binding.cardViewAdd.setOnClickListener {
            val title = holder.binding.txtTitle.text.toString()
            val amount = holder.binding.txtAmount.text.toString()

            when {
                title.isEmpty() -> {
                    CustomToastNotification(context, context.resources.getString(R.string.txt_alert_empty_other_investment_name))
                }
                model.imageUri == null -> {
                    CustomToastNotification(context, context.resources.getString(R.string.txt_alert_attach_other_investment_img))
                }
                amount.isEmpty() -> {
                    CustomToastNotification(context, context.resources.getString(R.string.txt_alert_other_investment_amount))
                }
                else -> {
                    model.title = title
                    model.amount = amount
                    clickListener.itemClick(model, position, "AddData")
                }
            }
        }
    }

    override fun getItemCount(): Int = itrList.size

    inner class ViewHolder(val binding: RowInvestmentDetailBinding) : RecyclerView.ViewHolder(binding.root)
}
