package com.app.rupiksha.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.reports.GetReportUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.AEPSReportDetailModel
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ReportDetailsViewModel @Inject constructor(
    private val getReportUseCase: GetReportUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _reportState = MutableStateFlow<Resource<List<AEPSReportDetailModel>>?>(null)
    val reportState: StateFlow<Resource<List<AEPSReportDetailModel>>?> = _reportState

    private var currentPage = 1
    private val pageLimit = 10
    private var totalRecords = 0
    private val allReports = mutableListOf<AEPSReportDetailModel>()

    fun getReports(type: String, fromDate: String, toDate: String, isInitial: Boolean = true) {
        if (isInitial) {
            currentPage = 1
            allReports.clear()
        }

        val headers = mapOf(
            "headerToken" to (storageUtil.getAccessToken() ?: ""),
            "headerKey" to storageUtil.getApiKey()
        )

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("from", fromDate)
            .addFormDataPart("to", toDate)
            .addFormDataPart("type", type)
            .addFormDataPart("page_number", currentPage.toString())
            .addFormDataPart("limit", pageLimit.toString())
            .build()

        viewModelScope.launch {
            if (isInitial) _reportState.value = Resource.Loading()
            
            val result = getReportUseCase(headers, requestBody)
            if (result is Resource.Success) {
                totalRecords = result.data?.totalRecord ?: 0
                val reports = when (type) {
                    "aeps" -> result.data?.data?.aepsReport
                    "money-transfer" -> result.data?.data?.dmtReport
                    "qtransfer" -> result.data?.data?.qtransferReport
                    "payout" -> result.data?.data?.payoutReport
                    "recharge" -> result.data?.data?.rechargeReport
                    "bbps" -> result.data?.data?.bbpsReport
                    "uti-coupon" -> result.data?.data?.utiReport
                    "wallet" -> result.data?.data?.walletReport
                    "wallet-to-wallet" -> result.data?.data?.walletToWalletReport
                    else -> emptyList()
                } ?: emptyList()
                
                allReports.addAll(reports)
                _reportState.value = Resource.Success(allReports.toList())
                currentPage++
            } else {
                _reportState.value = Resource.Error(result.message ?: "Failed to fetch reports")
            }
        }
    }

    fun canLoadMore(): Boolean {
        return allReports.size < totalRecords
    }
}