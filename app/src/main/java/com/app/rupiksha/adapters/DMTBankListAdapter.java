package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.DmtBanklistLayoutBinding;
import com.app.rupiksha.models.DMTBankdetailListModel;

import java.util.List;

public class DMTBankListAdapter extends RecyclerView.Adapter<DMTBankListAdapter.ViewHolder> {
    private Context context;
    private List<DMTBankdetailListModel> list;
    DMTBankListAdapter.OnItemClick onItemClick;

    public DMTBankListAdapter(Context context, List<DMTBankdetailListModel> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DmtBanklistLayoutBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.dmt_banklist_layout, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        holder.binding.valueaccountnumber.setText(list.get(position).getAccno());
        holder.binding.valuename.setText(list.get(position).getName());
        holder.binding.valuebankname.setText(list.get(position).getBankname());


         if(position==0){
             holder.binding.line2.setVisibility(View.VISIBLE);
             holder.binding.view1.setVisibility(View.VISIBLE);
         }else{
             holder.binding.line2.setVisibility(View.GONE);
             holder.binding.view1.setVisibility(View.GONE);
         }

//         holder.binding.valueaction.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//
//                 onItemClick.onClick(position);
//                 list.remove(position);
//             }
//         });

        holder.binding.imps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  String type="IMPS";
                onItemClick.onIMPSClick(type,position);

            }
        });

        holder.binding.neft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type="NEFT";
                onItemClick.onNEFTClick(type,position);

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private DmtBanklistLayoutBinding binding;

        public ViewHolder(@NonNull DmtBanklistLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
        public void onIMPSClick(String type,int position);
        public void onNEFTClick(String type,int position);
    }


}

