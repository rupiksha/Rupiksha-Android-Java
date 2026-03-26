package com.app.rupiksha.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.app.rupiksha.R;
import com.app.rupiksha.models.RechargeOperatorModel;

import java.util.List;

public class OperatorAdapter extends ArrayAdapter<RechargeOperatorModel>
{
    private LayoutInflater layoutInflater;
    private Context mContext;
    private int gravity;

    public OperatorAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<RechargeOperatorModel> objects, int center)
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
            convertView = layoutInflater.inflate(R.layout.row_operator_selector_item, parent, false);
        }
        RechargeOperatorModel spinnerItem = getItem(position);
        TextView itemName = convertView.findViewById(R.id.tvItemName);
        ImageView image = convertView.findViewById(R.id.image);
       // itemName.setGravity(gravity);
        itemName.setText(spinnerItem.getName());
        if (!spinnerItem.getImage().isEmpty()) {
            Glide.with(mContext).load(spinnerItem.getImage()).placeholder(R.color.white_60).into(image);
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_price_selector_item_dropdown, parent, false);
        }
        RechargeOperatorModel spinnerItem = getItem(position);
        TextView itemName = convertView.findViewById(R.id.tvItemName);
        itemName.setText(spinnerItem.getName());

        return convertView;
    }
}
