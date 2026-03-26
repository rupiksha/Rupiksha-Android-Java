package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.app.rupiksha.R;
import com.app.rupiksha.databinding.BannerRecycleviewListBinding;
import com.app.rupiksha.models.BannerModel;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {
    private Context context;
    private List<BannerModel> list;
    BannerAdapter.OnItemClick onItemClick;

    public BannerAdapter(Context context, List<BannerModel> listAnnonce, OnItemClick onItemClick) {
        this.context = context;
        this.list = listAnnonce;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BannerRecycleviewListBinding annoncesRecycleviewListBinding = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_recycleview_list, parent, false));
        return new ViewHolder(annoncesRecycleviewListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        if(!list.get(position).getFilepath().isEmpty())
        {
            Glide.with(context).load(list.get(position).getFilepath()).placeholder(R.color.white_60).into(holder.binding.image);
        }
        holder.binding.startView.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        holder.binding.endView.setVisibility(position == list.size() - 1 ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private BannerRecycleviewListBinding binding;

        public ViewHolder(@NonNull BannerRecycleviewListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
    public static interface OnItemClick {
        public void onClick(int position,int proUserId, String link);
    }
}
