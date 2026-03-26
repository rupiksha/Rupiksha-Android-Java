package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.FetchRechargeLayoutBinding;
import com.app.rupiksha.models.RechargeTransDetailModel;

import java.util.List;

public class RechargeTransactionDetailAdapter extends RecyclerView.Adapter<RechargeTransactionDetailAdapter.ViewHolder> {
    private Context context;
    private List<RechargeTransDetailModel> list;
    RechargeTransactionDetailAdapter.OnItemClick onItemClick;
    private int answerNum = -1;
    public RechargeTransactionDetailAdapter(Context context, List<RechargeTransDetailModel> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FetchRechargeLayoutBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.fetch_recharge_layout, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        holder.binding.mobile.setText(""+list.get(position).getMobile());
        holder.binding.date.setText(list.get(position).getDate());
        holder.binding.status.setText(list.get(position).getStatus());
        holder.binding.amount.setText(R.string.rupees+" "+""+list.get(position).getAmount());
        holder.binding.txId.setText(list.get(position).getTxnid());
        holder.binding.operatorName.setText(list.get(position).getOperatorName());

        if(list.get(position).getStatus().equalsIgnoreCase("FAILED")){
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_red));
        }else  if(list.get(position).getStatus().equalsIgnoreCase("SUCCESS")){
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_green));
        }else{
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_yellow));

        }

        holder.binding.viewmorelayout.setVisibility(position == answerNum ? View.VISIBLE : View.GONE);
        holder.binding.ivpaymentview.setRotation(position == answerNum ? 0 : 180);
        holder.binding.btnViewdetails.setOnClickListener(view -> {
            if(answerNum == position){
                answerNum = -1;
                holder.binding.tvtitleview.setText(context.getResources().getText(R.string.txt_view_more));
            }else{
                answerNum = position;
                holder.binding.tvtitleview.setText(context.getResources().getText(R.string.txt_view_less));
            }
            onItemClick.onClick(position);
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FetchRechargeLayoutBinding binding;

        public ViewHolder(@NonNull FetchRechargeLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
    }


}

