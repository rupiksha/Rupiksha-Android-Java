package com.app.rupiksha.presentation.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.repository.ReportRepository
import com.app.rupiksha.domain.repository.WalletRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.AEPSReportDetailModel
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.WalletToWalletReportModel
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val walletRepository: WalletRepository,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _walletReportState = MutableStateFlow<Resource<List<AEPSReportDetailModel>>?>(null)
    val walletReportState: StateFlow<Resource<List<AEPSReportDetailModel>>?> = _walletReportState

    private val _fetchUserState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val fetchUserState: StateFlow<Resource<BaseResponse>?> = _fetchUserState

    private val _transactionState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val transactionState: StateFlow<Resource<BaseResponse>?> = _transactionState

    private var currentPage = 1
    private val pageLimit = 10
    private var totalRecords = 0
    private val allReports = mutableListOf<AEPSReportDetailModel>()

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
            val result = reportRepository.getReport(headers, requestBody)
            if (result is Resource.Success) {
                totalRecords = result.data?.totalRecord ?: 0
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
            _fetchUserState.value = walletRepository.fetchUser(headers, requestBody)
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
            _transactionState.value = walletRepository.doWalletTransaction(headers, requestBody)
        }
    }

    fun canLoadMore(): Boolean = allReports.size < totalRecords

    private fun getHeaders() = mapOf(
        "headerToken" to (storageUtil.getAccessToken() ?: ""),
        "headerKey" to storageUtil.getApiKey()
    )
    
    fun resetStates() {
        _transactionState.value = null
        _fetchUserState.value = null
    }
}