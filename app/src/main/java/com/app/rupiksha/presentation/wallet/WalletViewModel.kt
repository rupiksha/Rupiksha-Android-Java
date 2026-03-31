package com.app.rupiksha.presentation.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.reports.GetReportUseCase
import com.app.rupiksha.domain.use_case.wallet.AddMoneyUseCase
import com.app.rupiksha.domain.use_case.wallet.DoWalletTransactionUseCase
import com.app.rupiksha.domain.use_case.wallet.FetchUserUseCase
import com.app.rupiksha.domain.use_case.wallet.GetAddFundBankListUseCase
import com.app.rupiksha.domain.use_case.wallet.GetWalletBalanceUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.AEPSReportDetailModel
import com.app.rupiksha.models.AddfundBankModel
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getReportUseCase: GetReportUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
    private val doWalletTransactionUseCase: DoWalletTransactionUseCase,
    private val getAddFundBankListUseCase: GetAddFundBankListUseCase,
    private val addMoneyUseCase: AddMoneyUseCase,
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _walletReportState = MutableStateFlow<Resource<List<AEPSReportDetailModel>>?>(null)
    val walletReportState: StateFlow<Resource<List<AEPSReportDetailModel>>?> = _walletReportState

    private val _fetchUserState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val fetchUserState: StateFlow<Resource<BaseResponse>?> = _fetchUserState

    private val _transactionState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val transactionState: StateFlow<Resource<BaseResponse>?> = _transactionState

    private val _bankListState = MutableStateFlow<Resource<List<AddfundBankModel>>?>(null)
    val bankListState: StateFlow<Resource<List<AddfundBankModel>>?> = _bankListState

    private val _addMoneyState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val addMoneyState: StateFlow<Resource<BaseResponse>?> = _addMoneyState

    private val _walletBalanceState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val walletBalanceState: StateFlow<Resource<BaseResponse>?> = _walletBalanceState

    private var currentPage = 1
    private val pageLimit = 10
    private var totalRecords = 0
    private val allReports = mutableListOf<AEPSReportDetailModel>()

    init {
        getWalletBalance()
    }

    fun getWalletBalance() {
        val headers = getHeaders()
        viewModelScope.launch {
            _walletBalanceState.value = Resource.Loading()
            _walletBalanceState.value = getWalletBalanceUseCase(headers)
        }
    }

    fun getWalletReport(fromDate: String, toDate: String, isInitial: Boolean = true) {
        if (isInitial) {
            currentPage = 1
            allReports.clear()
        }

        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("from", fromDate)
            .addFormDataPart("to", toDate)
            .addFormDataPart("type", "wallet-to-wallet")
            .addFormDataPart("page_number", currentPage.toString())
            .addFormDataPart("limit", pageLimit.toString())
            .build()

        viewModelScope.launch {
            if (isInitial) _walletReportState.value = Resource.Loading()
            val result = getReportUseCase(headers, requestBody)
            if (result is Resource.Success) {
                // totalRecords = result.data?.totalRecord ?: 0 // Adjust if base model is different
                val reports = result.data?.data?.walletToWalletReport ?: emptyList()
                allReports.addAll(reports)
                _walletReportState.value = Resource.Success(allReports.toList())
                currentPage++
            } else {
                _walletReportState.value = Resource.Error(result.message ?: "Error fetching report")
            }
        }
    }

    fun fetchUser(phone: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("phone", phone)
            .build()

        viewModelScope.launch {
            _fetchUserState.value = Resource.Loading()
            _fetchUserState.value = fetchUserUseCase(headers, requestBody)
        }
    }

    fun doTransaction(phone: String, amount: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("phone", phone)
            .addFormDataPart("amount", amount)
            .build()

        viewModelScope.launch {
            _transactionState.value = Resource.Loading()
            _transactionState.value = doWalletTransactionUseCase(headers, requestBody)
        }
    }

    fun getBankList() {
        val headers = getHeaders()
        viewModelScope.launch {
            _bankListState.value = Resource.Loading()
            val result = getAddFundBankListUseCase(headers)
            if (result is Resource.Success) {
                _bankListState.value = Resource.Success(result.data?.data?.addfundBanks ?: emptyList())
            } else {
                _bankListState.value = Resource.Error(result.message ?: "Error fetching bank list")
            }
        }
    }

    fun addMoney(
        amount: String,
        bankId: Int,
        utr: String,
        date: String,
        proofImage: MultipartBody.Part?
    ) {
        val headers = getHeaders()
        val map = HashMap<String, RequestBody>()
        map["amount"] = amount.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        map["bank"] = bankId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        map["rrn"] = utr.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        map["txn_date"] = date.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        viewModelScope.launch {
            _addMoneyState.value = Resource.Loading()
            val result = addMoneyUseCase(headers, map, proofImage)
            _addMoneyState.value = result
            if (result is Resource.Success) {
                getWalletBalance()
            }
        }
    }

    fun canLoadMore(): Boolean = allReports.size < totalRecords

    private fun getHeaders() = mapOf(
        "headerToken" to storageUtil.accessToken,
        "headerKey" to storageUtil.apiKey
    )
    
    fun resetStates() {
        _transactionState.value = null
        _fetchUserState.value = null
        _addMoneyState.value = null
    }
}
