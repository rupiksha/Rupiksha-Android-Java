package com.app.rupiksha.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.OperatorRecycleviewListBinding;
import com.app.rupiksha.models.ModelSubscriptionBiller;

import java.util.List;

public class SearchSubscriptionBillerAdapter extends RecyclerView.Adapter<SearchSubscriptionBillerAdapter.ViewHolder> {
    private Context context;
    private List<ModelSubscriptionBiller> list;
    OnItemClick onItemClick;

    public SearchSubscriptionBillerAdapter(Context context, List<ModelSubscriptionBiller> listAnnonce, OnItemClick onItemClick) {
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
        holder.binding.title.setText(list.get(position).getBiller());

        Log.e("TitleName--->",list.get(position).getBiller());
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
