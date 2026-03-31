package com.app.rupiksha.presentation.dmt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.repository.DmtRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.BankModel
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class DmtViewModel @Inject constructor(
    private val repository: DmtRepository,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _bankListState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val bankListState: StateFlow<Resource<BaseResponse>?> = _bankListState

    private val _dmtBanksState = MutableStateFlow<Resource<List<BankModel>>?>(null)
    val dmtBanksState: StateFlow<Resource<List<BankModel>>?> = _dmtBanksState

    private val _initiateTransactionState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val initiateTransactionState: StateFlow<Resource<BaseResponse>?> = _initiateTransactionState

    private val _doTransactionState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val doTransactionState: StateFlow<Resource<BaseResponse>?> = _doTransactionState

    private val _addAccountState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val addAccountState: StateFlow<Resource<BaseResponse>?> = _addAccountState

    fun getDmtAccountList() {
        val headers = getHeaders()
        val dmtKey = storageUtil.getDmtKey() ?: ""
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("dmtKey", dmtKey)
            .build()

        viewModelScope.launch {
            _bankListState.value = Resource.Loading()
            _bankListState.value = repository.getDmtAccountList(headers, body)
        }
    }

    fun getDmtBankList() {
        val headers = getHeaders()
        viewModelScope.launch {
            _dmtBanksState.value = Resource.Loading()
            val result = repository.getDmtBankList(headers)
            if (result is Resource.Success) {
                _dmtBanksState.value = Resource.Success(result.data?.data?.dmtBankList ?: emptyList())
            } else {
                _dmtBanksState.value = Resource.Error(result.message ?: "Error fetching banks")
            }
        }
    }

    fun initiateTransaction(beneId: String, amount: String) {
        val headers = getHeaders()
        val dmtKey = storageUtil.getDmtKey() ?: ""
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("dmtKey", dmtKey)
            .addFormDataPart("bene_id", beneId)
            .addFormDataPart("amount", amount)
            .addFormDataPart("mode", "IMPS")
            .build()

        viewModelScope.launch {
            _initiateTransactionState.value = Resource.Loading()
            _initiateTransactionState.value = repository.initiateDMTTransaction(headers, body)
        }
    }

    fun doTransaction(otp: String) {
        val headers = getHeaders()
        val dmtKey = storageUtil.getDmtKey() ?: ""
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("otp", otp)
            .addFormDataPart("dmtKey", dmtKey)
            .build()

        viewModelScope.launch {
            _doTransactionState.value = Resource.Loading()
            _doTransactionState.value = repository.doDMTTransaction(headers, body)
        }
    }

    fun addDmtAccount(bankId: String, accountNumber: String, ifsc: String, name: String, mobile: String) {
        val headers = getHeaders()
        val dmtKey = storageUtil.getDmtKey() ?: ""
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", name)
            .addFormDataPart("account", accountNumber)
            .addFormDataPart("ifsc", ifsc)
            .addFormDataPart("bid", bankId)
            .addFormDataPart("mobile", mobile)
            .addFormDataPart("dmtKey", dmtKey)
            .build()

        viewModelScope.launch {
            _addAccountState.value = Resource.Loading()
            _addAccountState.value = repository.addDmtAccount(headers, body)
        }
    }

    fun deleteAccount(beneId: String) {
        val headers = getHeaders()
        val dmtKey = storageUtil.getDmtKey() ?: ""
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("dmtKey", dmtKey)
            .addFormDataPart("beneId", beneId)
            .build()

        viewModelScope.launch {
            val result = repository.deleteDmtAccount(headers, body)
            if (result is Resource.Success) {
                getDmtAccountList()
            }
        }
    }

    private fun getHeaders() = mapOf(
        "headerToken" to (storageUtil.getAccessToken() ?: ""),
        "headerKey" to (storageUtil.getApiKey() ?: "")
    )

    fun resetStates() {
        _initiateTransactionState.value = null
        _doTransactionState.value = null
        _addAccountState.value = null
    }
}
