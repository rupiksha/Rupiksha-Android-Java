package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.app.rupiksha.R;
import com.app.rupiksha.databinding.RowFaqItemBinding;
import com.app.rupiksha.models.FaqList;

import java.util.List;

public class QuestionAnswerAdapter extends RecyclerView.Adapter<QuestionAnswerAdapter.ViewHolder> {
    private Context context;
    private OnItemClick onItemClick;
    private int answerNum = -1;
    List<FaqList> list;
    String label;

    public QuestionAnswerAdapter(Context context, List<FaqList> faqModels, String label, OnItemClick onItemClick) {
        this.context = context;
        this.onItemClick = onItemClick;
        this.list=faqModels;
        this.label=label;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowFaqItemBinding binding = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_faq_item, parent, false));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {



        if(label.equalsIgnoreCase("RECHARGE COMMISSION")){
            holder.binding.dash.setText(list.get(position).getOperator());
        }else if(label.equalsIgnoreCase("BBPS Commission")){
            holder.binding.dash.setText(list.get(position).getOpid());
        }else{
            holder.binding.from.setText(list.get(position).getFroma());
            holder.binding.to.setText(list.get(position).getToa());
        }

        if(list.get(position).getPercent().equals("1")) {
            holder.binding.amount.setText(list.get(position).getAmount()+ " %");
        }else{
            holder.binding.amount.setText(list.get(position).getAmount()+" "+ context.getResources().getString(R.string.rupees));
        }



         if(position==0){
             holder.binding.header.setVisibility(View.VISIBLE);
         }else{
             holder.binding.header.setVisibility(View.GONE);
         }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowFaqItemBinding binding;

        public ViewHolder(@NonNull RowFaqItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static interface OnItemClick {
        public void onClick(int position);
    }
}
