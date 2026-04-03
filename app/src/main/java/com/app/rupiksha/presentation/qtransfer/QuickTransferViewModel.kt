package com.app.rupiksha.presentation.qtransfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.qtransfer.*
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.*
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class QuickTransferViewModel @Inject constructor(
    private val getGlobalBankListUseCase: GetGlobalBankListUseCase,
    private val fetchAccountsUseCase: FetchAccountsUseCase,
    private val accountVerifyUseCase: AccountVerifyUseCase,
    private val initiateQtTransactionUseCase: InitiateQtTransactionUseCase,
    private val doQtTransactionUseCase: DoQtTransactionUseCase,
    private val storageUtil: StorageUtil,
    private val locationHelper: com.app.rupiksha.utils.LocationHelper
) : ViewModel() {

    private val _bankListState = MutableStateFlow<Resource<List<GlobalBankModel>>?>(null)
    val bankListState: StateFlow<Resource<List<GlobalBankModel>>?> = _bankListState

    private val _accountsState = MutableStateFlow<Resource<List<QtAccountModel>>?>(null)
    val accountsState: StateFlow<Resource<List<QtAccountModel>>?> = _accountsState

    private val _verifyState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val verifyState: StateFlow<Resource<BaseResponse>?> = _verifyState

    private val _initiateState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val initiateState: StateFlow<Resource<BaseResponse>?> = _initiateState

    private val _transactionState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val transactionState: StateFlow<Resource<BaseResponse>?> = _transactionState

    init {
        getBankList()
    }

    fun getBankList() {
        val headers = getHeaders()
        viewModelScope.launch {
            _bankListState.value = Resource.Loading()
            val result = getGlobalBankListUseCase(headers)
            if (result is Resource.Success) {
                _bankListState.value = Resource.Success(result.data?.data?.globalList ?: emptyList())
            } else {
                _bankListState.value = Resource.Error(result.message ?: "Error")
            }
        }
    }

    fun fetchAccounts(mobile: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("mobile", mobile)
            .build()

        viewModelScope.launch {
            _accountsState.value = Resource.Loading()
            val result = fetchAccountsUseCase(headers, requestBody)
            if (result is Resource.Success) {
                _accountsState.value = Resource.Success(result.data?.data?.qtAccounts ?: emptyList())
            } else {
                _accountsState.value = Resource.Error(result.message ?: "Error")
            }
        }
    }

    fun verifyAccount(account: String, ifsc: String, mobile: String) {
        val headers = getHeaders()

        viewModelScope.launch {
            _verifyState.value = Resource.Loading()
            val location = locationHelper.getCurrentLocation()
            val lat = location?.latitude?.toString() ?: "0.0"
            val long = location?.longitude?.toString() ?: "0.0"

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("account", account)
                .addFormDataPart("ifsc", ifsc)
                .addFormDataPart("mobile", mobile)
                .addFormDataPart("lat", lat)
                .addFormDataPart("log", long)
                .build()

            _verifyState.value = accountVerifyUseCase(headers, requestBody)
        }
    }

    fun initiateTransaction(mobile: String, amount: String, account: String, ifsc: String, name: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("mobile", mobile)
            .addFormDataPart("amount", amount)
            .addFormDataPart("account", account)
            .addFormDataPart("ifsc", ifsc)
            .addFormDataPart("name", name)
            .build()

        viewModelScope.launch {
            _initiateState.value = Resource.Loading()
            _initiateState.value = initiateQtTransactionUseCase(headers, requestBody)
        }
    }

    fun doTransaction(otp: String, txnKey: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("otp", otp)
            .addFormDataPart("txn_key", txnKey)
            .build()

        viewModelScope.launch {
            _transactionState.value = Resource.Loading()
            _transactionState.value = doQtTransactionUseCase(headers, requestBody)
        }
    }

    private fun getHeaders() = mapOf(
        "headerToken" to storageUtil.accessToken,
        "headerKey" to storageUtil.apiKey
    )

    fun resetStates() {
        _verifyState.value = null
        _initiateState.value = null
        _transactionState.value = null
    }
}
