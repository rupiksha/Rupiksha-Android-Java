package com.app.rupiksha.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.rupiksha.R
import com.app.rupiksha.databinding.RowBankDetailsBinding
import com.app.rupiksha.extra.CustomToastNotification
import com.app.rupiksha.interfaces.ItemClickListener
import com.app.rupiksha.models.AccountTypeModel
import com.app.rupiksha.models.BankDetailModel
import com.app.rupiksha.utils.Utils

class BankDetailAdapter(
    private val context: Context,
    private val list: List<BankDetailModel>,
    private val accountTypeModels: ArrayList<AccountTypeModel>,
    private val clickListener: ItemClickListener
) : RecyclerView.Adapter<BankDetailAdapter.ViewHolder>() {

    private var selectedAccountType: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowBankDetailsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_bank_details,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        if (model.imageUri != null) {
            Utils.setDocuments(context, holder.binding.imgBankStatement, holder.binding.txtBankStatement, model.imageUri)
        } else {
            holder.binding.txtBankStatement.visibility = View.GONE
            holder.binding.imgBankStatement.visibility = View.VISIBLE
            holder.binding.imgBankStatement.setImageDrawable(context.resources.getDrawable(R.drawable.bank_statment_des, null))
        }

        holder.binding.etAccountNumber.setText(model.AccountNumber ?: "")
        holder.binding.etBankName.setText(model.BankName ?: "")
        holder.binding.etIfscCode.setText(model.bankIfscCode ?: "")

        if (!model.bankType.isNullOrEmpty()) {
            holder.binding.etAccountType.setText(model.bankType)
        } else {
            holder.binding.etAccountType.setHint(R.string.placeholder_account_type)
        }

        setListData(accountTypeModels, position, holder.binding.etAccountType)

        holder.binding.llBankStatement.setOnClickListener {
            model.BankName = holder.binding.etBankName.text.toString()
            model.AccountNumber = holder.binding.etAccountNumber.text.toString()
            model.bankIfscCode = holder.binding.etIfscCode.text.toString()
            model.bankType = holder.binding.etAccountType.text.toString()

            clickListener.itemClick(model, position, "AddBankStatementImage")
        }

        holder.binding.btnAddDetail.setOnClickListener {
            val imgUploadBankStatementrName = model.imageName ?: ""
            val bankName = holder.binding.etBankName.text.toString()
            val accountNumber = holder.binding.etAccountNumber.text.toString()
            val ifscCode = holder.binding.etIfscCode.text.toString()
            val accountType = holder.binding.etAccountType.text.toString()

            when {
                bankName.isEmpty() -> CustomToastNotification(context, context.resources.getString(R.string.txt_bank_name))
                accountNumber.isEmpty() -> CustomToastNotification(context, context.resources.getString(R.string.txt_bank_account_number))
                ifscCode.isEmpty() -> CustomToastNotification(context, context.resources.getString(R.string.txt_bank_ifsc_code))
                accountType.isEmpty() -> CustomToastNotification(context, context.resources.getString(R.string.txt_bank_account_type))
                imgUploadBankStatementrName.isEmpty() -> CustomToastNotification(context, context.resources.getString(R.string.txt_empty_bank_statement_img))
                else -> {
                    model.BankName = bankName
                    model.AccountNumber = accountNumber
                    model.bankIfscCode = ifscCode
                    model.bankType = accountType
                    clickListener.itemClick(model, position, "AddBankDetail")
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(val binding: RowBankDetailsBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onClick(position: Int)
    }

    private fun setListData(accountTypeModels: ArrayList<AccountTypeModel>, position: Int, autoCompleteTextView: AutoCompleteTextView) {
        val genderSelectorAdapter = SectorSelectorAdapter(
            context, R.layout.row_type_selector_item,
            R.id.tvItemName, accountTypeModels, Gravity.CENTER
        )

        autoCompleteTextView.threshold = 100
        autoCompleteTextView.setAdapter(genderSelectorAdapter)
        autoCompleteTextView.setOnItemClickListener { _, _, position1, _ ->
            list[position].bankType = accountTypeModels[position1].title
            autoCompleteTextView.hint = ""
            autoCompleteTextView.setText(accountTypeModels[position1].title)
            autoCompleteTextView.clearFocus()
        }
    }
}
