package com.app.rupiksha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.rupiksha.R
import com.app.rupiksha.databinding.BannerRecycleviewListBinding
import com.app.rupiksha.models.BannerModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class BannerAdapter(
    private val context: Context,
    private val list: List<BannerModel>,
    private val onItemClick: OnItemClick? = null
) : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: BannerRecycleviewListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.banner_recycleview_list,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        if (!model.filepath.isNullOrEmpty()) {
            Glide.with(context)
                .load(model.filepath)
                .placeholder(R.color.white_60)
                .into(holder.binding.image)
        }
        
        holder.binding.startView.visibility = if (position == 0) View.VISIBLE else View.GONE
        holder.binding.endView.visibility = if (position == list.size - 1) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(val binding: BannerRecycleviewListBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onClick(position: Int, proUserId: Int, link: String)
    }
}
