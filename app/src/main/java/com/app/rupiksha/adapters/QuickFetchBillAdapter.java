package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.QicktransferBillLayoutBinding;
import com.app.rupiksha.models.QuickFetchBillModel;

import java.util.List;

public class QuickFetchBillAdapter extends RecyclerView.Adapter<QuickFetchBillAdapter.ViewHolder> {
    private Context context;
    private List<QuickFetchBillModel> list;
    QuickFetchBillAdapter.OnItemClick onItemClick;

    public QuickFetchBillAdapter(Context context, List<QuickFetchBillModel> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QicktransferBillLayoutBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.qicktransfer_bill_layout, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        holder.binding.date.setText(list.get(position).getDate());
        holder.binding.txId.setText(list.get(position).getTxnid());
        holder.binding.status.setText(list.get(position).getStatus());
        holder.binding.acountname.setText(list.get(position).getAccount());
        holder.binding.rrn.setText(list.get(position).getRrn());

        if(list.get(position).getStatus().equalsIgnoreCase("FAILED")){
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_red));
        }else{
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_green));

        }

        String wallet= String.valueOf(list.get(position).getAmount());
        holder.binding.amount.setText(context.getResources().getString(R.string.rupees) + " " +wallet);


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private QicktransferBillLayoutBinding binding;

        public ViewHolder(@NonNull QicktransferBillLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
    }


}

