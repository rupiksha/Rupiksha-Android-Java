package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.WalletToWalletReportLayoutBinding;
import com.app.rupiksha.models.WalletToWalletReportModel;

import java.util.List;

public class WalletToWalletRecentTxnAdapter extends RecyclerView.Adapter<WalletToWalletRecentTxnAdapter.ViewHolder> {
    private Context context;
    private List<WalletToWalletReportModel> list;
    WalletToWalletRecentTxnAdapter.OnItemClick onItemClick;
    private int answerNum = -1;
    public WalletToWalletRecentTxnAdapter(Context context, List<WalletToWalletReportModel> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WalletToWalletReportLayoutBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_to_wallet_report_layout, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        holder.binding.txnId.setText(""+list.get(position).getTxnid());
        holder.binding.date.setText(list.get(position).getDate());
        holder.binding.sndmob.setText(list.get(position).getSenderPhone());
        holder.binding.type.setText(list.get(position).getType());

        holder.binding.txSndname.setText(list.get(position).getSenderName());


        if(list.get(position).getType().equalsIgnoreCase("DEBIT")){
            holder.binding.type.setTextColor(context.getResources().getColor(R.color.color_red));
        }/*else if(list.get(position).getStatus().equalsIgnoreCase("ERROR")){
             holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_red));
        }else if(list.get(position).getStatus().equalsIgnoreCase("PENDING")){
          holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_yellow));
       }*/else{
            holder.binding.type.setTextColor(context.getResources().getColor(R.color.color_green));
        }
        String wallet= String.valueOf(list.get(position).getAmount());
        holder.binding.amount.setText(context.getResources().getString(R.string.rupees) + " " +wallet);
        holder.binding.rcname.setText(list.get(position).getReceiverName());
        holder.binding.rcmob.setText(list.get(position).getReceiverphone());
        holder.binding.viewmorelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onClick(position);
            }
        });

        holder.binding.viewmorelayout.setVisibility(position == answerNum ? View.VISIBLE : View.GONE);
        holder.binding.line8.setVisibility(position == answerNum ? View.VISIBLE : View.GONE);
        holder.binding.logo.setVisibility(position == answerNum ? View.VISIBLE : View.GONE);
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
        holder.binding.llShare.setOnClickListener(view -> {
            onItemClick.onShareReciept( holder.binding.recptlayout,position);
        });

        holder.binding.llDr.setOnClickListener(view -> {
            onItemClick.onDownloadReciept( holder.binding.recptlayout,position);
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private WalletToWalletReportLayoutBinding binding;

        public ViewHolder(@NonNull WalletToWalletReportLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
        public void onShareReciept(ConstraintLayout recptlayout, int position);
        public void onDownloadReciept(ConstraintLayout recptlayout, int position);

    }


}

