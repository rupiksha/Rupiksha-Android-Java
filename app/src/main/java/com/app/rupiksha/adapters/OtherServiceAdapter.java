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
import com.app.rupiksha.models.OtherServiceModel;

import java.util.List;

public class OtherServiceAdapter extends RecyclerView.Adapter<OtherServiceAdapter.ViewHolder> {
    private Context context;
    private List<OtherServiceModel> list;
    OtherServiceAdapter.OnItemClick onItemClick;

    public OtherServiceAdapter(Context context, List<OtherServiceModel> stepList, OnItemClick onItemClick) {
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
            case "irctc":
                holder.binding.icon.setImageResource(R.drawable.uti);
                break;
            case "flight":
                holder.binding.icon.setImageResource(R.drawable.uti);
                break;
            case "bus":
                holder.binding.icon.setImageResource(R.drawable.uti);
                break;
            case "hotel":
                holder.binding.icon.setImageResource(R.drawable.uti);
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

