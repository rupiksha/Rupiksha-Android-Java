package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.app.rupiksha.R;
import com.app.rupiksha.databinding.AepsRecycleviewListBinding;
import com.app.rupiksha.models.AepsServiceModel;

import java.util.List;

public class AepsServiceAdapter extends RecyclerView.Adapter<AepsServiceAdapter.ViewHolder> {
    private Context context;
    private List<AepsServiceModel> list;
    AepsServiceAdapter.OnItemClick onItemClick;

    public AepsServiceAdapter(Context context, List<AepsServiceModel> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AepsRecycleviewListBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.aeps_recycleview_list, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

/*        if (!list.get(position).getIcon().isEmpty()) {
            Glide.with(context).load(list.get(position).getIcon()).placeholder(R.color.white_60).into(holder.binding.icon);
        } else {
            holder.binding.icon.setImageResource(R.drawable.electricty);
        }*/

        holder.binding.title.setText(list.get(position).getName());
        switch (list.get(position).getType()) {
            case "cw":
                holder.binding.icon.setImageResource(R.drawable.cash_withdrwal);
                break;
            case "ap":
                holder.binding.icon.setImageResource(R.drawable.aadhar_pay);
                break;
            case "be":
                holder.binding.icon.setImageResource(R.drawable.balance_enquery);
                break;
            case "ms":
                holder.binding.icon.setImageResource(R.drawable.mini_statement);
                break;
            case "cd":
                holder.binding.icon.setImageResource(R.drawable.cash_withdrwal);
                break;
            case "matm":
                holder.binding.icon.setImageResource(R.drawable.matm_icon);
                break;
        }
        holder.binding.llRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onClick(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AepsRecycleviewListBinding binding;

        public ViewHolder(@NonNull AepsRecycleviewListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
    }


}

