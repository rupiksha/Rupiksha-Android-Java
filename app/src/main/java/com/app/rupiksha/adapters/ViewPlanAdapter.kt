package com.app.rupiksha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.rupiksha.R
import com.app.rupiksha.databinding.ViewPlanLayoutBinding
import com.app.rupiksha.models.PlanDetailModel

class ViewPlanAdapter(
    private val context: Context,
    private val list: List<PlanDetailModel>,
    private val onItemClick: OnItemClick
) : RecyclerView.Adapter<ViewPlanAdapter.ViewHolder>() {

    private var answerNum = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewPlanLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.view_plan_layout,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.binding.valuename.text = "${model.data}"
        holder.binding.valuesvalidity.text = "${model.validity}"
        holder.binding.amount.text = "${context.resources.getString(R.string.rupees)} ${model.rs}"
        holder.binding.description.text = model.desc

        holder.binding.amount.setOnClickListener {
            onItemClick.onClick(position)
        }
        
        setExpendableTextview(context, holder.binding.description, holder.binding.tvtitleview)
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(val binding: ViewPlanLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onClick(position: Int)
    }

    private fun setExpendableTextview(context: Context, textview: TextView, showMoreText: TextView) {
        textview.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                textview.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (textview.lineCount > 2) {
                    textview.maxLines = 2
                    textview.visibility = android.view.View.VISIBLE

                    showMoreText.setOnClickListener {
                        if (showMoreText.text.toString().equals("Show More", ignoreCase = true)) {
                            textview.maxLines = Int.MAX_VALUE
                            showMoreText.text = "Show Less"
                        } else {
                            textview.maxLines = 2
                            showMoreText.text = "Show More"
                        }
                    }
                }
            }
        })
    }
}
