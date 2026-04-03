package com.app.rupiksha.presentation.pan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.pan.VerifyPanUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.PanDetailsModel
import com.app.rupiksha.storage.StorageUtil
import com.app.rupiksha.utils.LocationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class PanVerificationViewModel @Inject constructor(
    private val verifyPanUseCase: VerifyPanUseCase,
    private val storageUtil: StorageUtil,
    private val locationHelper: LocationHelper
) : ViewModel() {

    private val _panDetailsState = MutableStateFlow<Resource<PanDetailsModel>?>(null)
    val panDetailsState: StateFlow<Resource<PanDetailsModel>?> = _panDetailsState

    fun verifyPan(panNumber: String) {
        val headers = mapOf(
            "headerToken" to storageUtil.accessToken,
            "headerKey" to storageUtil.apiKey
        )

        viewModelScope.launch {
            _panDetailsState.value = Resource.Loading()

            val location = locationHelper.getCurrentLocation()
            val lat = location?.latitude?.toString() ?: "0.0"
            val log = location?.longitude?.toString() ?: "0.0"

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("pan", panNumber)
                .addFormDataPart("lat", lat)
                .addFormDataPart("log", log)
                .build()

            val result = verifyPanUseCase(headers, requestBody)
            if (result is Resource.Success) {
                _panDetailsState.value = Resource.Success(result.data?.data?.panDetailsModel!!)
            } else {
                _panDetailsState.value = Resource.Error(result.message ?: "Verification failed")
            }
        }
    }

    fun resetState() {
        _panDetailsState.value = null
    }
}
