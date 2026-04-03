package com.app.rupiksha.presentation.payout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.payout.*
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.PayoutAccountModel
import com.app.rupiksha.storage.StorageUtil
import com.app.rupiksha.utils.LocationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class PayoutViewModel @Inject constructor(
    private val getPayoutBankListUseCase: GetPayoutBankListUseCase,
    private val initiatePayoutTransactionUseCase: InitiatePayoutTransactionUseCase,
    private val doPayoutTransactionUseCase: DoPayoutTransactionUseCase,
    private val deletePayoutAccountUseCase: DeletePayoutAccountUseCase,
    private val storageUtil: StorageUtil,
    private val locationHelper: LocationHelper
) : ViewModel() {

    private val _bankListState = MutableStateFlow<Resource<List<PayoutAccountModel>>?>(null)
    val bankListState: StateFlow<Resource<List<PayoutAccountModel>>?> = _bankListState

    private val _initiateTransactionState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val initiateTransactionState: StateFlow<Resource<BaseResponse>?> = _initiateTransactionState

    private val _transactionState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val transactionState: StateFlow<Resource<BaseResponse>?> = _transactionState

    fun getPayoutBankList() {
        val headers = getHeaders()
        viewModelScope.launch {
            _bankListState.value = Resource.Loading()
            val result = getPayoutBankListUseCase(headers)
            if (result is Resource.Success) {
                _bankListState.value = Resource.Success(result.data?.data?.payoutAccounts ?: emptyList())
            } else {
                _bankListState.value = Resource.Error(result.message ?: "Error")
            }
        }
    }

    fun initiateTransaction(mobile: String, bankId: Int, amount: String, senderName: String, mode: String) {
        val headers = getHeaders()
        
        viewModelScope.launch {
            _initiateTransactionState.value = Resource.Loading()
            
            val location = locationHelper.getCurrentLocation()
            val lat = location?.latitude?.toString() ?: "0.0"
            val log = location?.longitude?.toString() ?: "0.0"

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("mobile", mobile)
                .addFormDataPart("bid", bankId.toString())
                .addFormDataPart("amount", amount)
                .addFormDataPart("mode", mode)
                .addFormDataPart("sname", senderName)
                .addFormDataPart("lat", lat)
                .addFormDataPart("log", log)
                .addFormDataPart("account_type", "1")
                .build()

            _initiateTransactionState.value = initiatePayoutTransactionUseCase(headers, requestBody)
        }
    }

    fun doPayoutTransaction(otp: String, txnKey: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("otp", otp)
            .addFormDataPart("txn_key", txnKey)
            .build()

        viewModelScope.launch {
            _transactionState.value = Resource.Loading()
            _transactionState.value = doPayoutTransactionUseCase(headers, requestBody)
        }
    }

    fun deletePayoutAccount(id: Int) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id", id.toString())
            .build()

        viewModelScope.launch {
            val result = deletePayoutAccountUseCase(headers, requestBody)
            if (result is Resource.Success) {
                getPayoutBankList() // Refresh
            }
        }
    }

    private fun getHeaders() = mapOf(
        "headerToken" to storageUtil.accessToken,
        "headerKey" to storageUtil.apiKey
    )

    fun resetStates() {
        _initiateTransactionState.value = null
        _transactionState.value = null
    }
}
