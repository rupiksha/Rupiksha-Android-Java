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
import com.app.rupiksha.databinding.DmtReportLayoutBinding;
import com.app.rupiksha.models.AEPSReportDetailModel;

import java.util.List;

public class DMTReportDetailAdapter extends RecyclerView.Adapter<DMTReportDetailAdapter.ViewHolder> {
    private Context context;
    private List<AEPSReportDetailModel> list;
    DMTReportDetailAdapter.OnItemClick onItemClick;
    private int answerNum = -1;
    public DMTReportDetailAdapter(Context context, List<AEPSReportDetailModel> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DmtReportLayoutBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.dmt_report_layout, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        if(list.get(position).isProgressDelay())
            holder.binding.llProgressbar.setVisibility(View.VISIBLE);
        else
            holder.binding.llProgressbar.setVisibility(View.GONE);

        holder.binding.txnId.setText(""+list.get(position).getTxnid());
        holder.binding.date.setText(list.get(position).getDate());
        holder.binding.status.setText(list.get(position).getStatus());
        holder.binding.type.setText(list.get(position).getMode());
        holder.binding.bname.setText(list.get(position).getBname());
        holder.binding.bankName.setText(list.get(position).getAccount());
        holder.binding.msg.setText(list.get(position).getMessage());
        holder.binding.txIfsc.setText(list.get(position).getIfsc());
        holder.binding.bankUtr.setText(list.get(position).getUtr());

        if(list.get(position).getStatus().equalsIgnoreCase("FAILED")){
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_red));
        }else if(list.get(position).getStatus().equalsIgnoreCase("ERROR")){
             holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_red));
        }else if(list.get(position).getStatus().equalsIgnoreCase("PENDING")){
          holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_yellow));
    }else{
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_green));
        }
        String wallet= String.valueOf(list.get(position).getAmount());
        holder.binding.amount.setText(context.getResources().getString(R.string.rupees) + " " +wallet);

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
        private DmtReportLayoutBinding binding;

        public ViewHolder(@NonNull DmtReportLayoutBinding itemView) {
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

