package com.app.rupiksha.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.rupiksha.R;
import com.app.rupiksha.models.GlobalBankModel;

import java.util.List;

public class BankIFSCAdapter extends ArrayAdapter<GlobalBankModel>
{
    private LayoutInflater layoutInflater;
    private Context mContext;
    private int gravity;

    public BankIFSCAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<GlobalBankModel> objects, int center)
    {
        super(context, resource, textViewResourceId, objects);
        layoutInflater = ((Activity) context).getLayoutInflater();
        mContext = context;
        this.gravity = center;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_price_selector_item, parent, false);
        }
        GlobalBankModel spinnerItem = getItem(position);
        TextView itemName = convertView.findViewById(R.id.tvItemName);
        itemName.setGravity(gravity);
        itemName.setText(spinnerItem.getName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_price_selector_item_dropdown, parent, false);
        }
        GlobalBankModel spinnerItem = getItem(position);
        TextView itemName = convertView.findViewById(R.id.tvItemName);
        itemName.setText(spinnerItem.getName());

        return convertView;
    }
}
