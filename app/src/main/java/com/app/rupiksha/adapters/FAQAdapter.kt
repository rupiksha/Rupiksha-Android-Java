package com.app.rupiksha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.rupiksha.R
import com.app.rupiksha.databinding.RowFaqListItemBinding
import com.app.rupiksha.models.FaqModel

class FAQAdapter(
    private val context: Context,
    private val faqModels: List<FaqModel>,
    private val onItemClick: OnItemClick? = null
) : RecyclerView.Adapter<FAQAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowFaqListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_faq_list_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = faqModels[position]
        holder.binding.tvFaqListTitle.text = model.label

        model.tableData?.let { tableData ->
            holder.binding.rvFaqList.adapter = QuestionAnswerAdapter(
                context,
                tableData,
                model.label ?: "",
                object : QuestionAnswerAdapter.OnItemClick {
                    override fun onClick(pos: Int) {
                        holder.binding.rvFaqList.adapter?.notifyDataSetChanged()
                    }
                }
            )
        }
    }

    override fun getItemCount(): Int = faqModels.size

    inner class ViewHolder(val binding: RowFaqListItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onClick(position: Int)
    }
}
