package com.app.rupiksha.presentation.login.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.login.OtpVerifyUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val otpVerifyUseCase: OtpVerifyUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _otpVerifyState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val otpVerifyState: StateFlow<Resource<BaseResponse>?> = _otpVerifyState

    fun verifyOtp(logKey: String, otp: String) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("log_key", logKey)
            .addFormDataPart("otp", otp)
            .build()

        viewModelScope.launch {
            _otpVerifyState.value = Resource.Loading()
            val result = otpVerifyUseCase(requestBody)
            if (result is Resource.Success) {
                result.data?.let { body ->
                    body.headerToken?.let { storageUtil.accessToken = it }
                    body.headerKey?.let { storageUtil.apiKey = it }
                    body.dmtKey?.let { storageUtil.dmtKey = it }
                    body.data?.profile?.let { storageUtil.saveUserInfo(it) }
                }
            }
            _otpVerifyState.value = result
        }
    }
    
    fun resetState() {
        _otpVerifyState.value = null
    }
}
