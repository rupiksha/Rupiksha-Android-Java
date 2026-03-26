package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.AepsMinistatementLayoutBinding;
import com.app.rupiksha.models.Ministatement;

import java.util.List;

public class AepsMinistatementAdapter extends RecyclerView.Adapter<AepsMinistatementAdapter.ViewHolder> {
    private Context context;
    private List<Ministatement> list;
    AepsMinistatementAdapter.OnItemClick onItemClick;
    private int answerNum = -1;
    public AepsMinistatementAdapter(Context context, List<Ministatement> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AepsMinistatementLayoutBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.aeps_ministatement_layout, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(position==0){
            holder.binding.line2.setVisibility(View.VISIBLE);
        }else{
            holder.binding.line2.setVisibility(View.GONE);
        }
        holder.binding.txntype.setText(""+list.get(position).getTxnType());

           if(list.get(position).getTxnType().equalsIgnoreCase("Dr"))
           {
                 holder.binding.txntype.setTextColor(context.getResources().getColor(R.color.color_red));
                 holder.binding.amount.setTextColor(context.getResources().getColor(R.color.color_red));
           }else{
                  holder.binding.txntype.setTextColor(context.getResources().getColor(R.color.color_green));
                  holder.binding.amount.setTextColor(context.getResources().getColor(R.color.color_green));
            }
        holder.binding.amount.setText(context.getResources().getString(R.string.rupees) + " " +list.get(position).getAmount());
        holder.binding.date.setText(list.get(position).getDate());
        holder.binding.narration.setText(list.get(position).getNarration());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AepsMinistatementLayoutBinding binding;

        public ViewHolder(@NonNull AepsMinistatementLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
    }


}

