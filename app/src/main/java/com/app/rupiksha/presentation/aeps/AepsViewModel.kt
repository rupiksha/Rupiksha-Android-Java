package com.app.rupiksha.presentation.aeps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.aeps.*
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BankModel
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.DeviceModel
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AepsViewModel @Inject constructor(
    private val getBankListUseCase: GetBankListUseCase,
    private val aepsBalanceEnquiryUseCase: AepsBalanceEnquiryUseCase,
    private val aepsCashWithdrawalUseCase: AepsCashWithdrawalUseCase,
    private val aepsMiniStatementUseCase: AepsMiniStatementUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _bankListState = MutableStateFlow<Resource<List<BankModel>>?>(null)
    val bankListState: StateFlow<Resource<List<BankModel>>?> = _bankListState

    private val _deviceListState = MutableStateFlow<List<DeviceModel>>(emptyList())
    val deviceListState: StateFlow<List<DeviceModel>> = _deviceListState

    private val _transactionState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val transactionState: StateFlow<Resource<BaseResponse>?> = _transactionState

    init {
        getBankList()
        loadDevices()
    }

    private fun loadDevices() {
        // In the original app, these were static or from API, but often they are fixed for AEPS RD services
        _deviceListState.value = listOf(
            DeviceModel().apply { id = 1; name = "Mantra MFS110" },
            DeviceModel().apply { id = 2; name = "Morpho" },
            DeviceModel().apply { id = 3; name = "ACPL L1" },
            DeviceModel().apply { id = 4; name = "SecuGen" },
            DeviceModel().apply { id = 5; name = "Mantra MFS100" },
            DeviceModel().apply { id = 10; name = "Face Auth" }
        )
    }

    fun getBankList() {
        val headers = getHeaders()
        viewModelScope.launch {
            _bankListState.value = Resource.Loading()
            val result = getBankListUseCase(headers)
            if (result is Resource.Success) {
                _bankListState.value = Resource.Success(result.data?.data?.bank ?: emptyList())
            } else {
                _bankListState.value = Resource.Error(result.message ?: "Failed to load banks")
            }
        }
    }

    fun performTransaction(
        type: String,
        mobile: String,
        aadhar: String,
        bankId: Int,
        amount: String,
        pidData: String,
        lat: String,
        log: String,
        parsedData: Map<String, String>
    ) {
        val headers = getHeaders()
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("mobile", mobile)
            .addFormDataPart("aadhar", aadhar)
            .addFormDataPart("bank", bankId.toString())
            .addFormDataPart("lat", lat)
            .addFormDataPart("log", log)
            .addFormDataPart("errorInfo", "Success")
            
        // Adding parsed XML data fields as required by the API
        parsedData.forEach { (key, value) ->
            builder.addFormDataPart(key, value)
        }

        if (type == "Withdrawal" || type == "Aadhar Pay") {
            builder.addFormDataPart("amount", amount)
        }

        viewModelScope.launch {
            _transactionState.value = Resource.Loading()
            val result = when (type) {
                "Withdrawal" -> aepsCashWithdrawalUseCase(headers, builder.build())
                "Balance Enquiry" -> aepsBalanceEnquiryUseCase(headers, builder.build())
                "Mini Statement" -> aepsMiniStatementUseCase(headers, builder.build())
                else -> Resource.Error("Invalid transaction type")
            }
            _transactionState.value = result
        }
    }

    private fun getHeaders() = mapOf(
        "headerToken" to (storageUtil.getAccessToken() ?: ""),
        "headerKey" to storageUtil.getApiKey()
    )

    fun resetTransactionState() {
        _transactionState.value = null
    }
}
