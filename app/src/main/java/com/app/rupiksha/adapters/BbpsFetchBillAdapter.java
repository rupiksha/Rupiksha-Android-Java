package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.FetchBillLayoutBinding;
import com.app.rupiksha.models.BbpsFetchBillModel;

import java.text.DecimalFormat;
import java.util.List;

public class BbpsFetchBillAdapter extends RecyclerView.Adapter<BbpsFetchBillAdapter.ViewHolder> {
    private Context context;
    private List<BbpsFetchBillModel> list;
    BbpsFetchBillAdapter.OnItemClick onItemClick;

    public BbpsFetchBillAdapter(Context context, List<BbpsFetchBillModel> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FetchBillLayoutBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.fetch_bill_layout, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
            holder.binding.lblId.setText(""+list.get(position).getDisplayname());
            holder.binding.cId.setText(""+list.get(position).getCanumber());
            holder.binding.name.setText(""+list.get(position).getOperatorName());
            holder.binding.txId.setText(""+list.get(position).getTxnid());
            holder.binding.date.setText(""+list.get(position).getDate());

                String wallet= String.valueOf(list.get(position).getAmount());
                if(wallet==null || wallet.equals("")){
                    holder.binding.amount.setText(context.getResources().getString(R.string.rupees) + " " +wallet);
                }else {
                    double famount = list.get(position).getAmount();
                    holder.binding.amount.setText(context.getResources().getString(R.string.rupees) + " " + (new DecimalFormat("##.##").format(famount)));
                }
              holder.binding.status.setText(list.get(position).getStatus());
              if(list.get(position).getStatus().equalsIgnoreCase("FAILED"))
              {
                  holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_red));
              }else if(list.get(position).getStatus().equalsIgnoreCase("PENDING"))
              {
                  holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_blue));
              }else{
                  holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_green));
              }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FetchBillLayoutBinding binding;

        public ViewHolder(@NonNull FetchBillLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
    }


}

