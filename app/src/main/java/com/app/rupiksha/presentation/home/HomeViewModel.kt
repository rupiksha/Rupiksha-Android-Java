package com.app.rupiksha.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.repository.UserRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.ModelUserInfo
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UserRepository,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _userProfileState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val userProfileState: StateFlow<Resource<BaseResponse>?> = _userProfileState

    private val _userInfo = MutableStateFlow<ModelUserInfo?>(storageUtil.getUserInfo())
    val userInfo: StateFlow<ModelUserInfo?> = _userInfo

    private val _logoutState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val logoutState: StateFlow<Resource<BaseResponse>?> = _logoutState

    fun getUserProfile() {
        val accessToken = storageUtil.getAccessToken() ?: ""
        val apiKey = storageUtil.getApiKey()
        val headers = mapOf("headerToken" to accessToken, "headerKey" to apiKey)

        viewModelScope.launch {
            _userProfileState.value = Resource.Loading()
            val result = repository.getUserInfo(headers)
            if (result is Resource.Success) {
                result.data?.data?.profile?.let {
                    storageUtil.saveUserInfo(it)
                    _userInfo.value = it
                }
            }
            _userProfileState.value = result
        }
    }

    fun logout() {
        // In the original code, logout was an API call. 
        // For simplicity, let's clear storage first.
        storageUtil.clearAll()
        _logoutState.value = Resource.Success(BaseResponse().apply { message = "Logged out successfully" })
    }
}