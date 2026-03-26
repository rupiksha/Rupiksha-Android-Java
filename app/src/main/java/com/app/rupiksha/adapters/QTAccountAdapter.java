package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.QtAccountLayoutBinding;
import com.app.rupiksha.models.QtAccountModel;

import java.util.List;

public class QTAccountAdapter extends RecyclerView.Adapter<QTAccountAdapter.ViewHolder> {
    private Context context;
    private List<QtAccountModel> list;
    QTAccountAdapter.OnItemClick onItemClick;
    private int answerNum = -1;
    public QTAccountAdapter(Context context, List<QtAccountModel> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QtAccountLayoutBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.qt_account_layout, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.binding.bank.setText(list.get(position).getBname());
        holder.binding.account.setText(list.get(position).getAccount());
        holder.binding.txIfsc.setText(list.get(position).getIfsc());
        holder.binding.name.setText(list.get(position).getName());

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
        private QtAccountLayoutBinding binding;

        public ViewHolder(@NonNull QtAccountLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
    }


}

