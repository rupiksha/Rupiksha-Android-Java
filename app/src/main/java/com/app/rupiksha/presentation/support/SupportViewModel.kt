package com.app.rupiksha.presentation.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.support.GetSupportUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.SupportModel
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val getSupportUseCase: GetSupportUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _supportState = MutableStateFlow<Resource<SupportModel>?>(null)
    val supportState: StateFlow<Resource<SupportModel>?> = _supportState

    fun getSupportData() {
        val accessToken = storageUtil.getAccessToken() ?: ""
        val apiKey = storageUtil.getApiKey()
        val headers = mapOf("headerToken" to accessToken, "headerKey" to apiKey)

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
}