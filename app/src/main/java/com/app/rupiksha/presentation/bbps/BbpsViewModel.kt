package com.app.rupiksha.presentation.bbps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.bbps.FetchBillUseCase
import com.app.rupiksha.domain.use_case.bbps.GetBillerListUseCase
import com.app.rupiksha.domain.use_case.bbps.PayBillUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.BillerModel
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class BbpsViewModel @Inject constructor(
    private val getBillerListUseCase: GetBillerListUseCase,
    private val fetchBillUseCase: FetchBillUseCase,
    private val payBillUseCase: PayBillUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _billerListState = MutableStateFlow<Resource<List<BillerModel>>?>(null)
    val billerListState: StateFlow<Resource<List<BillerModel>>?> = _billerListState

    private val _fetchBillState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val fetchBillState: StateFlow<Resource<BaseResponse>?> = _fetchBillState

    private val _payBillState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val payBillState: StateFlow<Resource<BaseResponse>?> = _payBillState

    fun getBillerList(type: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("type", type)
            .build()

        viewModelScope.launch {
            _billerListState.value = Resource.Loading()
            val result = getBillerListUseCase(headers, requestBody)
            if (result is Resource.Success) {
                _billerListState.value = Resource.Success(result.data?.data?.billers ?: emptyList())
            } else {
                _billerListState.value = Resource.Error(result.message ?: "Failed to load billers")
            }
        }
    }

    fun fetchBill(billerId: Int, param1: String, param2: String, param3: String, param4: String, lat: String, log: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("billerId", billerId.toString())
            .addFormDataPart("lat", lat)
            .addFormDataPart("log", log)
            .addFormDataPart("param1", param1)
            .addFormDataPart("param2", param2)
            .addFormDataPart("param3", param3)
            .addFormDataPart("param4", param4)
            .build()

        viewModelScope.launch {
            _fetchBillState.value = Resource.Loading()
            _fetchBillState.value = fetchBillUseCase(headers, requestBody)
        }
    }

    fun payBill(billerId: Int, amount: String, param1: String, param2: String, param3: String, param4: String, skey: String, lat: String, log: String) {
        val headers = getHeaders()
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("billerId", billerId.toString())
            .addFormDataPart("amount", amount)
            .addFormDataPart("param1", param1)
            .addFormDataPart("param2", param2)
            .addFormDataPart("param3", param3)
            .addFormDataPart("param4", param4)
            .addFormDataPart("lat", lat)
            .addFormDataPart("log", log)
        
        if (skey.isNotEmpty()) {
            builder.addFormDataPart("skey", skey)
        }

        viewModelScope.launch {
            _payBillState.value = Resource.Loading()
            _payBillState.value = payBillUseCase(headers, builder.build())
        }
    }

    private fun getHeaders() = mapOf(
        "headerToken" to storageUtil.accessToken,
        "headerKey" to storageUtil.apiKey
    )
    
    fun resetStates() {
        _fetchBillState.value = null
        _payBillState.value = null
    }
}
