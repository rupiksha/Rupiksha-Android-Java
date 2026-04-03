package com.app.rupiksha.presentation.cms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.cms.GetCmsServiceUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CmsViewModel @Inject constructor(
    private val getCmsServiceUseCase: GetCmsServiceUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _cmsState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val cmsState: StateFlow<Resource<BaseResponse>?> = _cmsState

    fun getCmsService(mobile: String) {
        val headers = mapOf(
            "headerToken" to storageUtil.accessToken,
            "headerKey" to storageUtil.apiKey
        )

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("mobile", mobile)
            .build()

        viewModelScope.launch {
            _cmsState.value = Resource.Loading()
            _cmsState.value = getCmsServiceUseCase(headers, requestBody)
        }
    }

    fun resetCmsState() {
        _cmsState.value = null
    }
}
