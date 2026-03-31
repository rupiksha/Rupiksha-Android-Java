package com.app.rupiksha.presentation.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.repository.SupportRepository
import com.app.rupiksha.domain.use_case.support.GetSupportUseCase
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
class SupportViewModel @Inject constructor(
    private val getSupportUseCase: GetSupportUseCase,
    private val repository: SupportRepository,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _supportState = MutableStateFlow<Resource<SupportModel>?>(null)
    val supportState: StateFlow<Resource<SupportModel>?> = _supportState

    private val _supportTypesState = MutableStateFlow<Resource<List<SupportType>>?>(null)
    val supportTypesState: StateFlow<Resource<List<SupportType>>?> = _supportTypesState

    private val _ticketsState = MutableStateFlow<List<SupportTicket>>(emptyList())
    val ticketsState: StateFlow<List<SupportTicket>> = _ticketsState

    private val _createTicketState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val createTicketState: StateFlow<Resource<BaseResponse>?> = _createTicketState

    fun getSupportData() {
        val headers = getHeaders()
        viewModelScope.launch {
            _supportState.value = Resource.Loading()
            val result = getSupportUseCase(headers)
            if (result is Resource.Success) {
                _supportState.value = Resource.Success(result.data?.data?.support!!)
            } else {
                _supportState.value = Resource.Error(result.message ?: "Failed to fetch support data")
            }
        }
    }

    fun getSupportTypesAndTickets() {
        val headers = getHeaders()
        viewModelScope.launch {
            _supportTypesState.value = Resource.Loading()
            val result = repository.getSupportTypes(headers)
            if (result is Resource.Success) {
                _supportTypesState.value = Resource.Success(result.data?.data?.supportTypes ?: emptyList())
                _ticketsState.value = result.data?.data?.supportTickets ?: emptyList()
            } else {
                _supportTypesState.value = Resource.Error(result.message ?: "Error")
            }
        }
    }

    fun createTicket(serviceType: String, txnId: String, message: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("service", serviceType)
            .addFormDataPart("txnid", txnId)
            .addFormDataPart("message", message)
            .build()

        viewModelScope.launch {
            _createTicketState.value = Resource.Loading()
            val result = repository.createTicket(headers, requestBody)
            _createTicketState.value = result
            if (result is Resource.Success) {
                getSupportTypesAndTickets() // Refresh list
            }
        }
    }

    private fun getHeaders() = mapOf(
        "headerToken" to (storageUtil.getAccessToken() ?: ""),
        "headerKey" to storageUtil.getApiKey()
    )

    fun resetCreateState() {
        _createTicketState.value = null
    }
}
