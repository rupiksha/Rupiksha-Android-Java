package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.RowBankDetailsBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.interfaces.ItemClickListener;
import com.app.rupiksha.models.AccountTypeModel;
import com.app.rupiksha.models.BankDetailModel;
import com.app.rupiksha.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class BankDetailAdapter extends RecyclerView.Adapter<BankDetailAdapter.ViewHolder> {
    private Context context;
    private List<BankDetailModel> list;
    private ArrayList<AccountTypeModel> accountTypeModels;
    private ItemClickListener clickListener;
    private String selectedAccountType;

    public BankDetailAdapter(Context context, List<BankDetailModel> stepList, ArrayList<AccountTypeModel> accountTypeModels, ItemClickListener clickListener) {
        this.context = context;
        this.list = stepList;
        this.accountTypeModels = accountTypeModels;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowBankDetailsBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bank_details, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(list.get(position).getImageUri()!= null) {
            Utils.setDocuments(context,holder.binding.imgBankStatement,holder.binding.txtBankStatement,list.get(position).getImageUri());
        } else {
            holder.binding.txtBankStatement.setVisibility(View.GONE);
            holder.binding.imgBankStatement.setVisibility(View.VISIBLE);
            holder.binding.imgBankStatement.setImageDrawable(context.getResources().getDrawable(R.drawable.bank_statment_des, null));
        }

        if(list.get(position).getAccountNumber()!= null && !list.get(position).getAccountNumber().isEmpty())
            holder.binding.etAccountNumber.setText(list.get(position).getAccountNumber());
        else
            holder.binding.etAccountNumber.setText("");

        if(list.get(position).getBankName()!= null && !list.get(position).getBankName().isEmpty())
            holder.binding.etBankName.setText(list.get(position).getBankName());
        else
            holder.binding.etBankName.setText("");

        if(list.get(position).getBankIfscCode()!= null && !list.get(position).getBankIfscCode().isEmpty())
            holder.binding.etIfscCode.setText(list.get(position).getBankIfscCode());
        else
            holder.binding.etIfscCode.setText("");

        if(list.get(position).getBankType()!= null && !list.get(position).getBankType().isEmpty())
            holder.binding.etAccountType.setText(list.get(position).getBankType());
        else
            holder.binding.etAccountType.setHint(context.getResources().getString(R.string.placeholder_account_type));

        setListData(accountTypeModels, position, holder.binding.etAccountType);

        holder.binding.llBankStatement.setOnClickListener(v -> {
             list.get(position).setBankName(holder.binding.etBankName.getText().toString());
             list.get(position).setAccountNumber( holder.binding.etAccountNumber.getText().toString());
             list.get(position).setBankIfscCode(holder.binding.etIfscCode.getText().toString());
             list.get(position).setBankType(holder.binding.etAccountType.getText().toString());


            clickListener.itemClick(list.get(position),position,"AddBankStatementImage");
        });


        holder.binding.btnAddDetail.setOnClickListener(v -> {

        String imgUploadBankStatementrName = list.get(position).getImageName();
        if (holder.binding.etBankName.getText().toString().isEmpty())
            new CustomToastNotification(context, context.getResources().getString(R.string.txt_bank_name));
        else if ( holder.binding.etAccountNumber.getText().toString().isEmpty())
            new CustomToastNotification(context, context.getResources().getString(R.string.txt_bank_account_number));
        else if (holder.binding.etIfscCode.getText().toString().isEmpty())
            new CustomToastNotification(context, context.getResources().getString(R.string.txt_bank_ifsc_code));
        else if (holder.binding.etAccountType.getText().toString().isEmpty())
            new CustomToastNotification(context, context.getResources().getString(R.string.txt_bank_account_type));
        else if (imgUploadBankStatementrName.isEmpty())
            new CustomToastNotification(context, context.getResources().getString(R.string.txt_empty_bank_statement_img));
        else{
            list.get(position).setBankName(holder.binding.etBankName.getText().toString());
            list.get(position).setAccountNumber(holder.binding.etAccountNumber.getText().toString());
            list.get(position).setBankIfscCode(holder.binding.etIfscCode.getText().toString());
            list.get(position).setBankType(holder.binding.etAccountType.getText().toString());
            clickListener.itemClick(list.get(position),position,"AddBankDetail");
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowBankDetailsBinding binding;

        public ViewHolder(@NonNull RowBankDetailsBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick {
        public void onClick(int position);
    }

    public void setListData(ArrayList<AccountTypeModel> accountTypeModels, int position, AutoCompleteTextView autoCompleteTextView) {

        SectorSelectorAdapter genderSelectorAdapter = new SectorSelectorAdapter(
                context, R.layout.row_type_selector_item,
                R.id.tvItemName, accountTypeModels, Gravity.CENTER
        );

        autoCompleteTextView.setThreshold(100);
        autoCompleteTextView.setAdapter(genderSelectorAdapter);
        autoCompleteTextView.setOnItemClickListener((parent, view, position1, id) -> {

            list.get(position).setBankType(accountTypeModels.get(position1).getTitle());
            autoCompleteTextView.setHint("");
            autoCompleteTextView.setText(accountTypeModels.get(position1).getTitle());
            autoCompleteTextView.clearFocus();

        });
    }
}