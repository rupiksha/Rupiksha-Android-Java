package com.app.rupiksha.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.app.rupiksha.R
import com.app.rupiksha.models.PayoutAccountModel

class PayoutBankAdapter(
    context: Context,
    resource: Int,
    textViewResourceId: Int,
    objects: List<PayoutAccountModel>,
    private val gravity: Int
) : ArrayAdapter<PayoutAccountModel>(context, resource, textViewResourceId, objects) {

    private val layoutInflater: LayoutInflater = (context as Activity).layoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.row_price_selector_item, parent, false)
        val spinnerItem = getItem(position)
        val itemName = view.findViewById<TextView>(R.id.tvItemName)
        itemName.gravity = gravity
        itemName.text = spinnerItem?.account
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.row_price_selector_item_dropdown, parent, false)
        val spinnerItem = getItem(position)
        val itemName = view.findViewById<TextView>(R.id.tvItemName)
        itemName.text = spinnerItem?.account
        return view
    }
}
