package com.app.rupiksha.presentation.bbps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.bbps.GetBbpsReportsUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BbpsReportModel
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class BbpsReportViewModel @Inject constructor(
    private val getBbpsReportsUseCase: GetBbpsReportsUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _reportsState = MutableStateFlow<Resource<List<BbpsReportModel>>?>(null)
    val reportsState: StateFlow<Resource<List<BbpsReportModel>>?> = _reportsState

    fun getBbpsReports() {
        val headers = mapOf(
            "headerToken" to storageUtil.accessToken,
            "headerKey" to storageUtil.apiKey
        )
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("type", "bbpsreport")
            .build()

        viewModelScope.launch {
            _reportsState.value = Resource.Loading()
            val result = getBbpsReportsUseCase(headers, requestBody)
            if (result is Resource.Success) {
                _reportsState.value = Resource.Success(result.data?.data?.bbpsreport ?: emptyList())
            } else {
                _reportsState.value = Resource.Error(result.message ?: "Failed to fetch reports")
            }
        }
    }
}
