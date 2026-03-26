package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.app.rupiksha.R;
import com.app.rupiksha.databinding.AllReportRecycleviewListBinding;
import com.app.rupiksha.models.ReportTypeModel;

import java.util.List;

public class AllReportAdapter extends RecyclerView.Adapter<AllReportAdapter.ViewHolder> {
    private Context context;
    private List<ReportTypeModel> list;
    OnItemClick onItemClick;

    public  AllReportAdapter(Context context, List<ReportTypeModel> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllReportRecycleviewListBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_report_recycleview_list, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
       /* if (!list.get(position).getImage().isEmpty()) {
            Glide.with(context).load(list.get(position).getImage()).placeholder(R.color.white_60).into(holder.binding.icon);
        } else {
           // holder.binding.ivMacd.setImageResource(R.drawable.macd_img);
        }*/

        holder.binding.title.setText(list.get(position).getName());
         switch (list.get(position).getType()) {
            case "aeps":
                holder.binding.icon.setImageResource(R.drawable.aadhar_pay);
                break;
            case "money-transfer":
                holder.binding.icon.setImageResource(R.drawable.dmt);
                break;
            case "qtransfer":
                holder.binding.icon.setImageResource(R.drawable.q_transfar);
                break;
            case "payout":
                holder.binding.icon.setImageResource(R.drawable.payout);
                break;
            case "bbps":
                holder.binding.icon.setImageResource(R.drawable.bbps);
                break;
            case "recharge":
                holder.binding.icon.setImageResource(R.drawable.mobile_recharge);
                break;
            case "uti-coupon":
                holder.binding.icon.setImageResource(R.drawable.uti);
                break;
            case "wallet":
                holder.binding.icon.setImageResource(R.drawable.wallet_new);
                break;
            case "wallet-to-wallet":
                holder.binding.icon.setImageResource(R.drawable.wallet_to_wallet);
                break;
         }


        holder.binding.materialcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               onItemClick.onClick(position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AllReportRecycleviewListBinding binding;

        public ViewHolder(@NonNull AllReportRecycleviewListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
    }


}

