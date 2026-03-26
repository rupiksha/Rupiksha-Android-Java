package com.app.rupiksha.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.RowInvestmentDetailBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.interfaces.ItemClickListener;
import com.app.rupiksha.models.TaxFormDetailModel;

import java.util.List;

public class ITRAdapter extends RecyclerView.Adapter<ITRAdapter.ViewHolder> {
    private Context context;
    private List<TaxFormDetailModel> itrList;
    ItemClickListener clickListener;

    public ITRAdapter(Context context, List<TaxFormDetailModel> itrList, ItemClickListener clickListener) {
        this.context = context;
        this.itrList = itrList;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowInvestmentDetailBinding annoncesRecycleviewListBinding = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_investment_detail, parent, false));
        return new ViewHolder(annoncesRecycleviewListBinding);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        if(itrList.get(position).getImageName()!= null && !itrList.get(position).getImageName().isEmpty())
            holder.binding.txtNoFileChoose.setText(itrList.get(position).getImageName());
        else
            holder.binding.txtNoFileChoose.setText(context.getResources().getString(R.string.txt_no_file_chosen));

        if(itrList.get(position).getTitle()!= null && !itrList.get(position).getTitle().isEmpty())
            holder.binding.txtTitle.setText(itrList.get(position).getTitle());
        else
            holder.binding.txtTitle.setText("");

        if(itrList.get(position).getAmount()!= null && !itrList.get(position).getAmount().isEmpty())
            holder.binding.txtAmount.setText(itrList.get(position).getAmount());
        else
            holder.binding.txtAmount.setText("");


            holder.binding.cardViewChose.setOnClickListener(v -> {
                itrList.get(position).setTitle(holder.binding.txtTitle.getText().toString());
                itrList.get(position).setAmount(holder.binding.txtAmount.getText().toString());
                clickListener.itemClick(itrList.get(position),position,"AddImage");
            });

            holder.binding.cardViewAdd.setOnClickListener(v -> {
                if(holder.binding.txtTitle.getText().toString().isEmpty())
                    new CustomToastNotification(context,context.getResources().getString(R.string.txt_alert_empty_other_investment_name));
                else if(itrList.get(position).getImageUri()== null)
                    new CustomToastNotification(context,context.getResources().getString(R.string.txt_alert_attach_other_investment_img));
                else if(holder.binding.txtAmount.getText().toString().isEmpty())
                    new CustomToastNotification(context,context.getResources().getString(R.string.txt_alert_other_investment_amount));
                else{
                    itrList.get(position).setTitle(holder.binding.txtTitle.getText().toString());
                    itrList.get(position).setAmount(holder.binding.txtAmount.getText().toString());
                    clickListener.itemClick(itrList.get(position),position,"AddData");
                }

        });

    }

    @Override
    public int getItemCount() {
        return itrList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowInvestmentDetailBinding binding;

        public ViewHolder(@NonNull RowInvestmentDetailBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
