package com.app.rupiksha.presentation.dmt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.repository.DmtRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.BankModel
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
    private val repository: DmtRepository,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _bankListState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val bankListState: StateFlow<Resource<BaseResponse>?> = _bankListState

    private val _dmtBanksState = MutableStateFlow<Resource<List<BankModel>>?>(null)
    val dmtBanksState: StateFlow<Resource<List<BankModel>>?> = _dmtBanksState

    private val _initiateTransactionState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val initiateTransactionState: StateFlow<Resource<BaseResponse>?> = _initiateTransactionState

    private val _doTransactionState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val doTransactionState: StateFlow<Resource<BaseResponse>?> = _doTransactionState

    private val _addAccountState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val addAccountState: StateFlow<Resource<BaseResponse>?> = _addAccountState

    private val _remitterLoginState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val remitterLoginState: StateFlow<Resource<BaseResponse>?> = _remitterLoginState

    private val _remitterRegisterState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val registerState: StateFlow<Resource<BaseResponse>?> = _remitterRegisterState

    private val _biometricVerifyState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val biometricVerifyState: StateFlow<Resource<BaseResponse>?> = _biometricVerifyState

    private val _validateAadharState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val validateAadharState: StateFlow<Resource<BaseResponse>?> = _validateAadharState

    private val _validateOtpState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val validateOtpState: StateFlow<Resource<BaseResponse>?> = _validateOtpState

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
            DeviceModel().apply { id = 5; name = "Mantra MFS100" },
            DeviceModel().apply { id = 10; name = "Face Auth" }
        )
    }

    fun remitterLogin(mobile: String) {
        val headers = getHeaders()
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("mobile", mobile)
            .build()

        viewModelScope.launch {
            _remitterLoginState.value = Resource.Loading()
            val result = repository.remitterLogin(headers, body)
            if (result is Resource.Success) {
                result.data?.dmtKey?.let {
                    storageUtil.dmtKey = it
                }
            }
            _remitterLoginState.value = result
        }
    }

    fun remitterRegister(
        name: String, city: String, state: String, pincode: String,
        district: String, address: String, dob: String, mobile: String, dmtKey: String
    ) {
        val headers = getHeaders()
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("mobile", mobile)
            .addFormDataPart("fname", name)
            .addFormDataPart("lname", "")
            .addFormDataPart("city", city)
            .addFormDataPart("state", state)
            .addFormDataPart("pincode", pincode)
            .addFormDataPart("district", district)
            .addFormDataPart("address", address)
            .addFormDataPart("dob", dob)
            .addFormDataPart("dmtKey", dmtKey)
            .build()

        viewModelScope.launch {
            _remitterRegisterState.value = Resource.Loading()
            _remitterRegisterState.value = repository.remitterRegister(headers, body)
        }
    }

    fun validateAadhar(aadhar: String, mobile: String) {
        val headers = getHeaders()
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("aadhar", aadhar)
            .addFormDataPart("mobile", mobile)
            .addFormDataPart("dmtKey", storageUtil.dmtKey)
            .build()

        viewModelScope.launch {
            _validateAadharState.value = Resource.Loading()
            _validateAadharState.value = repository.validateAadhar(headers, body)
        }
    }

    fun validateOtp(otp: String, otpId: String, aadhar: String, mobile: String, lat: String, log: String) {
        val headers = getHeaders()
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("otp", otp)
            .addFormDataPart("otpid", otpId)
            .addFormDataPart("aadhar", aadhar)
            .addFormDataPart("mobile", mobile)
            .addFormDataPart("lat", lat)
            .addFormDataPart("log", log)
            .addFormDataPart("dmtKey", storageUtil.dmtKey)
            .build()

        viewModelScope.launch {
            _validateOtpState.value = Resource.Loading()
            _validateOtpState.value = repository.validateOtp(headers, body)
        }
    }

    fun getDmtAccountList() {
        val headers = getHeaders()
        val dmtKey = storageUtil.dmtKey
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("dmtKey", dmtKey)
            .build()

        viewModelScope.launch {
            _bankListState.value = Resource.Loading()
            _bankListState.value = repository.getDmtAccountList(headers, body)
        }
    }

    fun getDmtBankList() {
        val headers = getHeaders()
        viewModelScope.launch {
            _dmtBanksState.value = Resource.Loading()
            val result = repository.getDmtBankList(headers)
            if (result is Resource.Success) {
                _dmtBanksState.value = Resource.Success(result.data?.data?.bank ?: emptyList())
            } else {
                _dmtBanksState.value = Resource.Error(result.message ?: "Error fetching banks")
            }
        }
    }

    fun initiateTransaction(beneId: String, amount: String) {
        val headers = getHeaders()
        val dmtKey = storageUtil.dmtKey
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("dmtKey", dmtKey)
            .addFormDataPart("bene_id", beneId)
            .addFormDataPart("amount", amount)
            .addFormDataPart("mode", "IMPS")
            .build()

        viewModelScope.launch {
            _initiateTransactionState.value = Resource.Loading()
            _initiateTransactionState.value = repository.initiateDMTTransaction(headers, body)
        }
    }

    fun doTransaction(otp: String) {
        val headers = getHeaders()
        val dmtKey = storageUtil.dmtKey
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("otp", otp)
            .addFormDataPart("dmtKey", dmtKey)
            .build()

        viewModelScope.launch {
            _doTransactionState.value = Resource.Loading()
            _doTransactionState.value = repository.doDMTTransaction(headers, body)
        }
    }

    fun addDmtAccount(bankId: String, accountNumber: String, ifsc: String, name: String, mobile: String) {
        val headers = getHeaders()
        val dmtKey = storageUtil.dmtKey
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", name)
            .addFormDataPart("account", accountNumber)
            .addFormDataPart("ifsc", ifsc)
            .addFormDataPart("bid", bankId)
            .addFormDataPart("mobile", mobile)
            .addFormDataPart("dmtKey", dmtKey)
            .build()

        viewModelScope.launch {
            _addAccountState.value = Resource.Loading()
            _addAccountState.value = repository.addDmtAccount(headers, body)
        }
    }

    fun deleteAccount(beneId: String) {
        val headers = getHeaders()
        val dmtKey = storageUtil.dmtKey
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("dmtKey", dmtKey)
            .addFormDataPart("beneId", beneId)
            .build()

        viewModelScope.launch {
            val result = repository.deleteDmtAccount(headers, body)
            if (result is Resource.Success) {
                getDmtAccountList()
            }
        }
    }

    fun biometricVerify(mobile: String, aadhar: String, fingerData: String, otpId: String) {
        val headers = getHeaders()
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("mobile", mobile)
            .addFormDataPart("aadhar", aadhar)
            .addFormDataPart("fingerData", fingerData)
            .addFormDataPart("otpid", otpId)
            .addFormDataPart("dmtKey", storageUtil.dmtKey)
            .build()

        viewModelScope.launch {
            _biometricVerifyState.value = Resource.Loading()
            _biometricVerifyState.value = repository.biometricVerify(headers, body)
        }
    }

    private fun getHeaders() = mapOf(
        "headerToken" to storageUtil.accessToken,
        "headerKey" to storageUtil.apiKey
    )

    fun resetStates() {
        _initiateTransactionState.value = null
        _doTransactionState.value = null
        _addAccountState.value = null
        _remitterLoginState.value = null
        _remitterRegisterState.value = null
        _biometricVerifyState.value = null
        _validateAadharState.value = null
        _validateOtpState.value = null
    }
}
