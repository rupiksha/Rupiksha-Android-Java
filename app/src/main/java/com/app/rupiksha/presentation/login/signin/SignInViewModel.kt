package com.app.rupiksha.presentation.login.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.login.ForgetPasswordUseCase
import com.app.rupiksha.domain.use_case.login.ForgetPinUseCase
import com.app.rupiksha.domain.use_case.login.LoginUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val forgetPasswordUseCase: ForgetPasswordUseCase,
    private val forgetPinUseCase: ForgetPinUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val loginState: StateFlow<Resource<BaseResponse>?> = _loginState

    private val _forgetPasswordState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val forgetPasswordState: StateFlow<Resource<BaseResponse>?> = _forgetPasswordState

    private val _forgetPinState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val forgetPinState: StateFlow<Resource<BaseResponse>?> = _forgetPinState

    fun login(phone: String, password: String, imei: String, device: String, ltype: String) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("phone", phone)
            .addFormDataPart("password", password)
            .addFormDataPart("imei", imei)
            .addFormDataPart("device", device)
            .addFormDataPart("ltype", ltype)
            .build()

        viewModelScope.launch {
            _loginState.value = Resource.Loading()
            _loginState.value = loginUseCase(requestBody)
        }
    }

    fun forgetPassword(email: String) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .build()

        viewModelScope.launch {
            _forgetPasswordState.value = Resource.Loading()
            _forgetPasswordState.value = forgetPasswordUseCase(requestBody)
        }
    }

    fun forgetPin(email: String) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .build()

        viewModelScope.launch {
            _forgetPinState.value = Resource.Loading()
            _forgetPinState.value = forgetPinUseCase(requestBody)
        }
    }
    
    fun resetStates() {
        _loginState.value = null
        _forgetPasswordState.value = null
        _forgetPinState.value = null
    }
}