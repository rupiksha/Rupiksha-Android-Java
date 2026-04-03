package com.app.rupiksha.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.app.rupiksha.R
import com.app.rupiksha.models.RechargeOperatorModel
import com.bumptech.glide.Glide
import androidx.core.content.ContextCompat

class OperatorAdapter(
    context: Context,
    resource: Int,
    textViewResourceId: Int,
    objects: List<RechargeOperatorModel>,
    private val gravity: Int
) : ArrayAdapter<RechargeOperatorModel>(context, resource, textViewResourceId, objects) {

    private val layoutInflater: LayoutInflater = (context as Activity).layoutInflater
    private val mContext: Context = context

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.row_operator_selector_item, parent, false)
        val spinnerItem = getItem(position)
        val itemName = view.findViewById<TextView>(R.id.tvItemName)
        val image = view.findViewById<ImageView>(R.id.image)

        itemName.text = spinnerItem?.name
        spinnerItem?.image?.let {
            if (it.isNotEmpty()) {
                Glide.with(mContext)
                    .load(it)
                    .placeholder(R.color.white_60)
                    .into(image)
            }
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.row_price_selector_item_dropdown, parent, false)
        val spinnerItem = getItem(position)
        val itemName = view.findViewById<TextView>(R.id.tvItemName)
        itemName.text = spinnerItem?.name

        return view
    }
}
