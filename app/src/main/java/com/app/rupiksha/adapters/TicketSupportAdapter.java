package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.SupportListLayoutBinding;
import com.app.rupiksha.models.SupportTicket;

import java.util.List;

public class TicketSupportAdapter extends RecyclerView.Adapter<TicketSupportAdapter.ViewHolder> {
    private Context context;
    private List<SupportTicket> list;
    TicketSupportAdapter.OnItemClick onItemClick;

    public TicketSupportAdapter(Context context, List<SupportTicket> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SupportListLayoutBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.support_list_layout, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

            holder.binding.cId.setText(""+list.get(position).getTicketid());
            holder.binding.servicename.setText(""+list.get(position).getService());
            holder.binding.txId.setText(""+list.get(position).getTxnid());
            holder.binding.date.setText(""+list.get(position).getDate());
            holder.binding.status.setText(list.get(position).getStatus());
            holder.binding.tvMessage.setText(list.get(position).getMessage());
            holder.binding.tvadmin.setText(list.get(position).getAdminmsg());
              if(list.get(position).getStatus().equalsIgnoreCase("ClOSED"))
              {
                  holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_red));
              }else if(list.get(position).getStatus().equalsIgnoreCase("PENDING"))
              {
                  holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_yellow));
              }else{
                  holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_green));
              }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SupportListLayoutBinding binding;

        public ViewHolder(@NonNull SupportListLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
    }


}

