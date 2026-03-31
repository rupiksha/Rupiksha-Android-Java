package com.app.rupiksha.presentation.uti

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.aeps.GetStateListUseCase
import com.app.rupiksha.domain.use_case.uti.BuyCouponUseCase
import com.app.rupiksha.domain.use_case.uti.GetUtiStatusUseCase
import com.app.rupiksha.domain.use_case.uti.PsaRegistrationUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.StateModel
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
class UtiViewModel @Inject constructor(
    private val getUtiStatusUseCase: GetUtiStatusUseCase,
    private val psaRegistrationUseCase: PsaRegistrationUseCase,
    private val buyCouponUseCase: BuyCouponUseCase,
    private val getStateListUseCase: GetStateListUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _utiStatusState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val utiStatusState: StateFlow<Resource<BaseResponse>?> = _utiStatusState

    private val _registrationState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val registrationState: StateFlow<Resource<BaseResponse>?> = _registrationState

    private val _buyCouponState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val buyCouponState: StateFlow<Resource<BaseResponse>?> = _buyCouponState

    private val _stateListState = MutableStateFlow<Resource<List<StateModel>>?>(null)
    val stateListState: StateFlow<Resource<List<StateModel>>?> = _stateListState

    fun getUtiStatus() {
        val headers = getHeaders()
        viewModelScope.launch {
            _utiStatusState.value = Resource.Loading()
            _utiStatusState.value = getUtiStatusUseCase(headers)
        }
    }

    fun getStateList() {
        viewModelScope.launch {
            _stateListState.value = Resource.Loading()
            val result = getStateListUseCase()
            if (result is Resource.Success) {
                _stateListState.value = Resource.Success(result.data?.data?.state ?: emptyList())
            } else {
                _stateListState.value = Resource.Error(result.message ?: "Failed to load states")
            }
        }
    }

    fun psaRegistration(
        name: String,
        aadhar: String,
        email: String,
        phone: String,
        pan: String,
        address: String,
        shopLocation: String,
        pincode: String,
        stateId: Int
    ) {
        val headers = getHeaders()
        val map = mapOf(
            "name" to name.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "aadhar" to aadhar.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "email" to email.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "phone" to phone.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "pan" to pan.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "address" to address.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "slocation" to shopLocation.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "pincode" to pincode.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "state" to stateId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        )

        viewModelScope.launch {
            _registrationState.value = Resource.Loading()
            _registrationState.value = psaRegistrationUseCase(headers, map)
        }
    }

    fun buyCoupon(qty: String) {
        val headers = getHeaders()
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("qty", qty)
            .build()

        viewModelScope.launch {
            _buyCouponState.value = Resource.Loading()
            _buyCouponState.value = buyCouponUseCase(headers, body)
        }
    }

    private fun getHeaders() = mapOf(
        "headerToken" to (storageUtil.getAccessToken() ?: ""),
        "headerKey" to storageUtil.getApiKey()
    )

    fun resetStates() {
        _registrationState.value = null
        _buyCouponState.value = null
    }
}
