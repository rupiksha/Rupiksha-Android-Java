package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.BbpsRecycleviewListBinding;
import com.app.rupiksha.models.BbpsServiceModel;

import java.util.List;

public class BbpsServiceAdapter extends RecyclerView.Adapter<BbpsServiceAdapter.ViewHolder> {
    private Context context;
    private List<BbpsServiceModel> list;
    BbpsServiceAdapter.OnItemClick onItemClick;

    public BbpsServiceAdapter(Context context, List<BbpsServiceModel> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BbpsRecycleviewListBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.bbps_recycleview_list, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
     /*   if (!list.get(position).getIcon().isEmpty()) {
            Glide.with(context).load(list.get(position).getIcon()).placeholder(R.color.white_60).into(holder.binding.icon);
        } else {
            holder.binding.icon.setImageResource(R.drawable.electricty);
        }*/
        holder.binding.title.setText(list.get(position).getName());

        switch (list.get(position).getName()) {
            case "Water":
                holder.binding.icon.setImageResource(R.drawable.water);
                break;
            case "Traffic Challan":
                holder.binding.icon.setImageResource(R.drawable.traffic_icon);
                break;
            case "Municipality":
                holder.binding.icon.setImageResource(R.drawable.muncipality);
                break;
            case "Municipal Corporation":
                holder.binding.icon.setImageResource(R.drawable.municipal_taxes);
                break;
            case "Mobile Prepaid":
                holder.binding.icon.setImageResource(R.drawable.mobile_recharge_icon);
                break;
            case "Mobile (Postpaid)":
                holder.binding.icon.setImageResource(R.drawable.mobile_recharge);
                break;
            case "LPG":
                holder.binding.icon.setImageResource(R.drawable.gas);
                break;
            case "Landline":
                holder.binding.icon.setImageResource(R.drawable.landline);
                break;
            case "Insurance":
                holder.binding.icon.setImageResource(R.drawable.insurance);
                break;
            case "Hospital":
                holder.binding.icon.setImageResource(R.drawable.hospital);
                break;
            case "Gas":
                holder.binding.icon.setImageResource(R.drawable.gas_sylinder);
                break;
            case "Fee Payment":
                holder.binding.icon.setImageResource(R.drawable.fee_payment);
                break;
            case "EMI Payment":
                holder.binding.icon.setImageResource(R.drawable.emi);
                break;
            case "EMI":
                holder.binding.icon.setImageResource(R.drawable.emi);
                break;
            case "Electricity":
                holder.binding.icon.setImageResource(R.drawable.electricty);
                break;
            case "DTH":
                holder.binding.icon.setImageResource(R.drawable.dth);
                break;
            case "Digital Voucher":
                holder.binding.icon.setImageResource(R.drawable.digital);
                break;
            case "Datacard Prepaid":
                holder.binding.icon.setImageResource(R.drawable.housing_society);
                break;
            case "Cable":
                holder.binding.icon.setImageResource(R.drawable.cable);
                break;
            case "Broadband Postpaid":
                holder.binding.icon.setImageResource(R.drawable.broadband);
                break;
            case "Broadband":
                holder.binding.icon.setImageResource(R.drawable.broadband);
                break;
            case "Tax":
                holder.binding.icon.setImageResource(R.drawable.municipality_taxes_icon);
                break;
            case "Credit Card":
                holder.binding.icon.setImageResource(R.drawable.credit_card_icon);
                break;
            case "Landline Postpaid":
                holder.binding.icon.setImageResource(R.drawable.landline);
                break;
        }

        holder.binding.title.setText(list.get(position).getName());
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
        private BbpsRecycleviewListBinding binding;

        public ViewHolder(@NonNull BbpsRecycleviewListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
    }


}

