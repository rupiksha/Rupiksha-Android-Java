package com.app.rupiksha.presentation.dmt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.dmt.*
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.DeviceModel
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class DmtViewModel @Inject constructor(
    private val remitterLoginUseCase: RemitterLoginUseCase,
    private val remitterRegisterUseCase: RemitterRegisterUseCase,
    private val getDmtBankListUseCase: GetDmtBankListUseCase,
    private val addDmtAccountUseCase: AddDmtAccountUseCase,
    private val initiateDmtTransactionUseCase: InitiateDmtTransactionUseCase,
    private val doDmtTransactionUseCase: DoDmtTransactionUseCase,
    private val validateAadharUseCase: ValidateAadharUseCase,
    private val validateOtpUseCase: ValidateOtpUseCase,
    private val biometricVerifyUseCase: BiometricVerifyUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val loginState: StateFlow<Resource<BaseResponse>?> = _loginState

    private val _registerState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val registerState: StateFlow<Resource<BaseResponse>?> = _registerState

    private val _bankListState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val bankListState: StateFlow<Resource<BaseResponse>?> = _bankListState

    private val _addAccountState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val addAccountState: StateFlow<Resource<BaseResponse>?> = _addAccountState

    private val _initiateTransactionState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val initiateTransactionState: StateFlow<Resource<BaseResponse>?> = _initiateTransactionState

    private val _doTransactionState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val doTransactionState: StateFlow<Resource<BaseResponse>?> = _doTransactionState

    private val _validateAadharState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val validateAadharState: StateFlow<Resource<BaseResponse>?> = _validateAadharState

    private val _validateOtpState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val validateOtpState: StateFlow<Resource<BaseResponse>?> = _validateOtpState

    private val _biometricVerifyState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val biometricVerifyState: StateFlow<Resource<BaseResponse>?> = _biometricVerifyState

    private val _deviceListState = MutableStateFlow<List<DeviceModel>>(emptyList())
    val deviceListState: StateFlow<List<DeviceModel>> = _deviceListState

    init {
        loadDevices()
    }

    private fun loadDevices() {
        _deviceListState.value = listOf(
            DeviceModel().apply { id = 1; name = "Mantra MFS110" },
            DeviceModel().apply { id = 2; name = "Morpho" },
            DeviceModel().apply { id = 3; name = "ACPL L1" },
            DeviceModel().apply { id = 4; name = "Aratek" },
            DeviceModel().apply { id = 5; name = "Mantra MFS100" }
        )
    }

    fun remitterLogin(phone: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("phone", phone)
            .build()

        viewModelScope.launch {
            _loginState.value = Resource.Loading()
            _loginState.value = remitterLoginUseCase(headers, requestBody)
        }
    }

    fun remitterRegister(name: String, city: String, state: String, pin: String, district: String, area: String, dob: String, phone: String, dmtKey: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", name)
            .addFormDataPart("city", city)
            .addFormDataPart("state", state)
            .addFormDataPart("pin", pin)
            .addFormDataPart("district", district)
            .addFormDataPart("area", area)
            .addFormDataPart("dob", dob)
            .addFormDataPart("mobile", phone)
            .addFormDataPart("dmtKey", dmtKey)
            .build()

        viewModelScope.launch {
            _registerState.value = Resource.Loading()
            _registerState.value = remitterRegisterUseCase(headers, requestBody)
        }
    }

    fun validateAadhar(aadhar: String, mobile: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("aadhar", aadhar)
            .addFormDataPart("mobile", mobile)
            .build()

        viewModelScope.launch {
            _validateAadharState.value = Resource.Loading()
            _validateAadharState.value = validateAadharUseCase(headers, requestBody)
        }
    }

    fun validateOtp(otp: String, otpId: String, aadhar: String, mobile: String, lat: String, log: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("otp", otp)
            .addFormDataPart("otpid", otpId)
            .addFormDataPart("aadhar", aadhar)
            .addFormDataPart("lat", lat)
            .addFormDataPart("log", log)
            .addFormDataPart("mobile", mobile)
            .build()

        viewModelScope.launch {
            _validateOtpState.value = Resource.Loading()
            _validateOtpState.value = validateOtpUseCase(headers, requestBody)
        }
    }

    fun biometricVerify(mobile: String, aadhar: String, fingerData: String, otpId: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("mobile", mobile)
            .addFormDataPart("aadhar", aadhar)
            .addFormDataPart("newFingerData", fingerData)
            .addFormDataPart("otpid", otpId)
            .build()

        viewModelScope.launch {
            _biometricVerifyState.value = Resource.Loading()
            _biometricVerifyState.value = biometricVerifyUseCase(headers, requestBody)
        }
    }

    fun getDmtBankList() {
        val headers = getHeaders()
        viewModelScope.launch {
            _bankListState.value = Resource.Loading()
            _bankListState.value = getDmtBankListUseCase(headers)
        }
    }

    fun addDmtAccount(bankId: String, accountNumber: String, ifsc: String, name: String, mobile: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("bank", bankId)
            .addFormDataPart("account", accountNumber)
            .addFormDataPart("ifsc", ifsc)
            .addFormDataPart("name", name)
            .addFormDataPart("mobile", mobile)
            .build()

        viewModelScope.launch {
            _addAccountState.value = Resource.Loading()
            _addAccountState.value = addDmtAccountUseCase(headers, requestBody)
        }
    }

    fun initiateTransaction(beneficiaryId: String, amount: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("beneficiary_id", beneficiaryId)
            .addFormDataPart("amount", amount)
            .build()

        viewModelScope.launch {
            _initiateTransactionState.value = Resource.Loading()
            _initiateTransactionState.value = initiateDmtTransactionUseCase(headers, requestBody)
        }
    }

    fun doTransaction(beneficiaryId: String, amount: String, pin: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("beneficiary_id", beneficiaryId)
            .addFormDataPart("amount", amount)
            .addFormDataPart("pin", pin)
            .build()

        viewModelScope.launch {
            _doTransactionState.value = Resource.Loading()
            _doTransactionState.value = doDmtTransactionUseCase(headers, requestBody)
        }
    }

    private fun getHeaders() = mapOf(
        "headerToken" to (storageUtil.getAccessToken() ?: ""),
        "headerKey" to storageUtil.getApiKey()
    )

    fun resetStates() {
        _loginState.value = null
        _registerState.value = null
        _addAccountState.value = null
        _initiateTransactionState.value = null
        _doTransactionState.value = null
        _validateAadharState.value = null
        _validateOtpState.value = null
        _biometricVerifyState.value = null
    }
}
