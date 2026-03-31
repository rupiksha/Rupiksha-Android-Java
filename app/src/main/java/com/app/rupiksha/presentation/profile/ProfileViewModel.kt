package com.app.rupiksha.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.profile.ChangePasswordUseCase
import com.app.rupiksha.domain.use_case.profile.ChangePinUseCase
import com.app.rupiksha.domain.use_case.profile.GetUserInfoUseCase
import com.app.rupiksha.domain.use_case.profile.UpdateProfileUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.KycModel
import com.app.rupiksha.models.ModelUserInfo
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
class ProfileViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val changePinUseCase: ChangePinUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _userInfoState = MutableStateFlow<ModelUserInfo?>(storageUtil.getUserInfo())
    val userInfoState: StateFlow<ModelUserInfo?> = _userInfoState

    private val _kycInfoState = MutableStateFlow<KycModel?>(storageUtil.getKYCInfo())
    val kycInfoState: StateFlow<KycModel?> = _kycInfoState

    private val _updateProfileState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val updateProfileState: StateFlow<Resource<BaseResponse>?> = _updateProfileState

    fun getUserInfo() {
        val headers = getHeaders()
        viewModelScope.launch {
            val result = getUserInfoUseCase(headers)
            if (result is Resource.Success) {
                val profile = result.data?.data?.profile
                if (profile != null) {
                    storageUtil.saveUserInfo(profile)
                    _userInfoState.value = profile
                }
                val kyc = result.data?.data?.kyc
                if (kyc != null) {
                    storageUtil.saveKYCInfo(kyc)
                    _kycInfoState.value = kyc
                }
            }
        }
    }

    fun updateProfile(name: String, mobile: String, email: String, profileImage: MultipartBody.Part?) {
        val headers = getHeaders()
        val partMap = mutableMapOf<String, RequestBody>()
        partMap["name"] = name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        partMap["mobile"] = mobile.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        partMap["email"] = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        viewModelScope.launch {
            _updateProfileState.value = Resource.Loading()
            val result = updateProfileUseCase(headers, partMap, profileImage)
            _updateProfileState.value = result
            if (result is Resource.Success) {
                getUserInfo() // Refresh local data
            }
        }
    }

    fun changePassword(current: String, new: String) {
        val headers = getHeaders()
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("current_password", current)
            .addFormDataPart("new_password", new)
            .build()

        viewModelScope.launch {
            _updateProfileState.value = Resource.Loading()
            _updateProfileState.value = changePasswordUseCase(headers, body)
        }
    }

    fun changePin(current: String, new: String) {
        val headers = getHeaders()
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("current_pin", current)
            .addFormDataPart("new_pin", new)
            .build()

        viewModelScope.launch {
            _updateProfileState.value = Resource.Loading()
            _updateProfileState.value = changePinUseCase(headers, body)
        }
    }

    private fun getHeaders() = mapOf(
        "headerToken" to (storageUtil.getAccessToken() ?: ""),
        "headerKey" to storageUtil.getApiKey()
    )

    fun resetUpdateState() {
        _updateProfileState.value = null
    }
}
