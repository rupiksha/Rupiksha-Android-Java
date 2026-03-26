package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.app.rupiksha.R;
import com.app.rupiksha.databinding.RowFaqListItemBinding;
import com.app.rupiksha.models.FaqModel;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.ViewHolder> {
    private Context context;
    List<FaqModel> faqModels;
    public FAQAdapter(Context context, List<FaqModel> faqList, OnItemClick onItemClick) {
        this.context = context;
        this.faqModels=faqList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowFaqListItemBinding binding = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_faq_list_item, parent, false));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.binding.tvFaqListTitle.setText(faqModels.get(position).getLabel());

        holder.binding.rvFaqList.setAdapter(new QuestionAnswerAdapter(context,faqModels.get(position).getTableData(),faqModels.get(position).getLabel(),new QuestionAnswerAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                holder.binding.rvFaqList.getAdapter().notifyDataSetChanged();
            }
        }));
    }

    @Override
    public int getItemCount() {
        return faqModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowFaqListItemBinding binding;

        public ViewHolder(@NonNull RowFaqListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static interface OnItemClick {
        public void onClick(int position);
    }
}
