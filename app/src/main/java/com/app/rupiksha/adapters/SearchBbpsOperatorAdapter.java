package com.app.rupiksha.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.databinding.OperatorRecycleviewListBinding;
import com.app.rupiksha.models.BillerModel;
import com.app.rupiksha.R;


import java.util.List;

public class SearchBbpsOperatorAdapter extends RecyclerView.Adapter<SearchBbpsOperatorAdapter.ViewHolder> {
    private Context context;
    private List<BillerModel> list;
    OnItemClick onItemClick;

    public SearchBbpsOperatorAdapter(Context context, List<BillerModel> listAnnonce, OnItemClick onItemClick) {
        this.context = context;
        this.list = listAnnonce;
        this.onItemClick=onItemClick;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OperatorRecycleviewListBinding annoncesRecycleviewListBinding = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.operator_recycleview_list, parent, false));
        return new ViewHolder(annoncesRecycleviewListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.binding.title.setText(list.get(position).getName());

        Log.e("TitleName--->",list.get(position).getName());
        holder.binding.llRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private OperatorRecycleviewListBinding binding;

        public ViewHolder(@NonNull OperatorRecycleviewListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
    public static interface OnItemClick {
        public void onClick(int position);

    }
}
