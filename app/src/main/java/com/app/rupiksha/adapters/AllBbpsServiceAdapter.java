package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.AllBbpsRecycleviewListBinding;
import com.app.rupiksha.models.BbpsServiceModel;

import java.util.List;

public class AllBbpsServiceAdapter extends RecyclerView.Adapter<AllBbpsServiceAdapter.ViewHolder> {
    private Context context;
    private List<BbpsServiceModel> list;
    AllBbpsServiceAdapter.OnItemClick onItemClick;

    public AllBbpsServiceAdapter(Context context, List<BbpsServiceModel> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllBbpsRecycleviewListBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_bbps_recycleview_list, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
       /* if (!list.get(position).getIcon().isEmpty()) {
            Glide.with(context).load(list.get(position).getIcon()).placeholder(R.color.white_60).into(holder.binding.icon);
        } else {
            holder.binding.icon.setImageResource(R.drawable.electricty);
        }*/

        holder.binding.title.setText(list.get(position).getName());
        switch (list.get(position).getName()) {
            case "Water":
                holder.binding.icon.setImageResource(R.drawable.water);
                break;
            case "eChallan":
                holder.binding.icon.setImageResource(R.drawable.echallan);
                break;
            case "Municipal Services":
                holder.binding.icon.setImageResource(R.drawable.municipality_services_icon);
                break;
            case "Tax":
                holder.binding.icon.setImageResource(R.drawable.municipality_taxes_icon);
                break;
            case "Municipal Taxes":
                holder.binding.icon.setImageResource(R.drawable.municipality_taxes_icon);
                break;
            case "Mobile Prepaid":
                holder.binding.icon.setImageResource(R.drawable.mobile_recharge_icon);
                break;
            case "Mobile Postpaid":
                holder.binding.icon.setImageResource(R.drawable.mobile_postpaid_icon);
                break;
            case "LPG":
                holder.binding.icon.setImageResource(R.drawable.gas);
                break;
            case "Landline Postpaid":
                holder.binding.icon.setImageResource(R.drawable.landline);
                break;
            case "Insurance":
                holder.binding.icon.setImageResource(R.drawable.insurance);
                break;
            case "Hospital":
                holder.binding.icon.setImageResource(R.drawable.hospital);
                break;
            case "Gas":
                holder.binding.icon.setImageResource(R.drawable.gas);
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
                holder.binding.icon.setImageResource(R.drawable.ticket);
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
            case "Rental Payment":
                holder.binding.icon.setImageResource(R.drawable.rentel);
                break;
            case "FASTag":
                holder.binding.icon.setImageResource(R.drawable.fastage_icon);
                break;
            case "Loan":
                holder.binding.icon.setImageResource(R.drawable.personal_loan);
                break;
            case "LPG Cylinder":
                holder.binding.icon.setImageResource(R.drawable.gas_sylinder);
                break;
            case "Cable TV":
                holder.binding.icon.setImageResource(R.drawable.cable_tv_icon);
                break;
            case "Education":
                holder.binding.icon.setImageResource(R.drawable.education);
                break;
            case "Subscription":
                holder.binding.icon.setImageResource(R.drawable.subscription);
                break;
            case "Clubs and Associations":
                holder.binding.icon.setImageResource(R.drawable.clubassociation);
                break;
            case "Housing Society":
                holder.binding.icon.setImageResource(R.drawable.housing_society);
                break;
            case "Credit Card":
                holder.binding.icon.setImageResource(R.drawable.credit_card_icon);
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
        private AllBbpsRecycleviewListBinding binding;

        public ViewHolder(@NonNull AllBbpsRecycleviewListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
    }


}

