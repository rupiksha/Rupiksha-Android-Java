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
import com.app.rupiksha.models.AccountTypeModel;

import java.util.List;

public class SectorSelectorAdapter extends ArrayAdapter<AccountTypeModel> {

    private LayoutInflater layoutInflater;
    private Context mContext;
    private int gravity;

    public SectorSelectorAdapter(@NonNull Context context, int resource, int textViewResourceId,
                                 @NonNull List<AccountTypeModel> objects, int gravity) {
        super(context, resource, textViewResourceId, objects);
        layoutInflater = ((Activity) context).getLayoutInflater();
        mContext = context;
        this.gravity = gravity;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_type_selector_item, parent, false);
        }

        AccountTypeModel spinnerItem = getItem(position);
        TextView itemName = convertView.findViewById(R.id.tvItemName);
        View viewSeparator = convertView.findViewById(R.id.viewSeparator);
        itemName.setGravity(gravity);
        itemName.setText(spinnerItem.getTitle());
        viewSeparator.setVisibility(View.GONE);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_type_selector_item, parent, false);
        }
        AccountTypeModel spinnerItem = getItem(position);
        TextView itemName = convertView.findViewById(R.id.tvItemName);
        View viewSeparator = convertView.findViewById(R.id.viewSeparator);

        itemName.setText(spinnerItem.getTitle());
        viewSeparator.setVisibility(View.GONE);

        return convertView;
    }



}
