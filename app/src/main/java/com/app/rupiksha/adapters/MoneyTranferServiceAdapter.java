package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.MoneyRecycleviewListBinding;
import com.app.rupiksha.models.MoneyTransferServiceModel;

import java.util.List;

public class MoneyTranferServiceAdapter extends RecyclerView.Adapter<MoneyTranferServiceAdapter.ViewHolder> {
    private Context context;
    private List<MoneyTransferServiceModel> list;
    MoneyTranferServiceAdapter.OnItemClick onItemClick;

    public MoneyTranferServiceAdapter(Context context, List<MoneyTransferServiceModel> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MoneyRecycleviewListBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.money_recycleview_list, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
      /*  if (!list.get(position).getIcon().isEmpty()) {
            Glide.with(context).load(list.get(position).getIcon()).placeholder(R.color.white_60).into(holder.binding.icon);
        } else {
            holder.binding.icon.setImageResource(R.drawable.payout);
        }*/
        holder.binding.title.setText(list.get(position).getName());

        switch (list.get(position).getType()) {
            case "rech":
                holder.binding.icon.setImageResource(R.drawable.mobile_recharge);
                break;
            case "dmt":
                holder.binding.icon.setImageResource(R.drawable.dmt);
                break;
            case "payout":
                holder.binding.icon.setImageResource(R.drawable.payout);
                break;
            case "qt":
                holder.binding.icon.setImageResource(R.drawable.q_transfar);
                break;
            case "uti":
                holder.binding.icon.setImageResource(R.drawable.uti);
                break;
            case "matm":
                holder.binding.icon.setImageResource(R.drawable.matm_icon);
                break;
            case "cms2":
                holder.binding.icon.setImageResource(R.drawable.cms);
                break;
        }

      /*  if(position==0) {
            holder.binding.title.setText("Mobile Prepaid");
            holder.binding.icon.setImageResource(R.drawable.mobile_prepaid);
        }else if(position==1) {
            holder.binding.title.setText("Quick Transfer");
            holder.binding.icon.setImageResource(R.drawable.quick_transfer);
       }else if(position==2){
            holder.binding.title.setText("DMT");
            holder.binding.icon.setImageResource(R.drawable.dmt);
        }else if(position==3){

            holder.binding.title.setText("Payout");
            holder.binding.icon.setImageResource(R.drawable.payout);
        }*/

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
        private MoneyRecycleviewListBinding binding;

        public ViewHolder(@NonNull MoneyRecycleviewListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
    }


}

