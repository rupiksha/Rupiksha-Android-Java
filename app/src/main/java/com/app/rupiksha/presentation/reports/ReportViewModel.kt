package com.app.rupiksha.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.reports.GetReportTypesUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.ReportTypeModel
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getReportTypesUseCase: GetReportTypesUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _reportTypesState = MutableStateFlow<Resource<List<ReportTypeModel>>?>(null)
    val reportTypesState: StateFlow<Resource<List<ReportTypeModel>>?> = _reportTypesState

    fun getReportTypes() {
        val headers = mapOf(
            "headerToken" to (storageUtil.getAccessToken() ?: ""),
            "headerKey" to storageUtil.getApiKey()
        )

        viewModelScope.launch {
            _reportTypesState.value = Resource.Loading()
            val result = getReportTypesUseCase(headers)
            if (result is Resource.Success) {
                _reportTypesState.value = Resource.Success(result.data?.data?.reportItems ?: emptyList())
            } else {
                _reportTypesState.value = Resource.Error(result.message ?: "Failed to fetch reports")
            }
        }
    }
}