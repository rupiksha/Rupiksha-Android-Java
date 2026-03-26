package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.BanklistLayoutBinding;
import com.app.rupiksha.models.PayoutAccountModel;

import java.util.List;

public class BankListAdapter extends RecyclerView.Adapter<BankListAdapter.ViewHolder> {
    private Context context;
    private List<PayoutAccountModel> list;
    BankListAdapter.OnItemClick onItemClick;

    public BankListAdapter(Context context, List<PayoutAccountModel> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BanklistLayoutBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.banklist_layout, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(position==0){
            holder.binding.line2.setVisibility(View.VISIBLE);
        }else{
            holder.binding.line2.setVisibility(View.GONE);
        }
        holder.binding.valueaccountnumber.setText(list.get(position).getAccount());
        holder.binding.valuename.setText(list.get(position).getName());
        holder.binding.valuestatus.setText(list.get(position).getStatus());

        holder.binding.valueaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onClick(position);
                list.remove(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private BanklistLayoutBinding binding;

        public ViewHolder(@NonNull BanklistLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
    }


}

