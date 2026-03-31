package com.app.rupiksha.presentation.login.user_kyc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.aeps.GetStateListUseCase
import com.app.rupiksha.domain.use_case.login.GetDocumentListUseCase
import com.app.rupiksha.domain.use_case.login.SubmitUserKycUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.StateModel
import com.app.rupiksha.models.AddressProof
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UserKycViewModel @Inject constructor(
    private val getStateListUseCase: GetStateListUseCase,
    private val getDocumentListUseCase: GetDocumentListUseCase,
    private val submitUserKycUseCase: SubmitUserKycUseCase
) : ViewModel() {

    private val _statesState = MutableStateFlow<Resource<List<StateModel>>>(Resource.Loading())
    val statesState: StateFlow<Resource<List<StateModel>>> = _statesState

    private val _documentsState = MutableStateFlow<Resource<List<AddressProof>>>(Resource.Loading())
    val documentsState: StateFlow<Resource<List<AddressProof>>> = _documentsState

    private val _kycSubmitState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val kycSubmitState: StateFlow<Resource<BaseResponse>?> = _kycSubmitState

    init {
        getStates()
        getDocuments()
    }

    fun getStates() {
        viewModelScope.launch {
            _statesState.value = Resource.Loading()
            val result = getStateListUseCase()
            if (result is Resource.Success) {
                _statesState.value = Resource.Success(result.data?.data?.state ?: emptyList())
            } else {
                _statesState.value = Resource.Error(result.message ?: "An error occurred")
            }
        }
    }

    fun getDocuments() {
        viewModelScope.launch {
            _documentsState.value = Resource.Loading()
            val result = getDocumentListUseCase()
            if (result is Resource.Success) {
                _documentsState.value = Resource.Success(result.data?.data?.addressProofList ?: emptyList())
            } else {
                _documentsState.value = Resource.Error(result.message ?: "An error occurred")
            }
        }
    }

    fun submitKyc(
        headers: Map<String, String>,
        map: Map<String, RequestBody>,
        panImage: MultipartBody.Part?,
        aadharFront: MultipartBody.Part?,
        aadharBack: MultipartBody.Part?,
        docImage: MultipartBody.Part?,
        selfie: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            _kycSubmitState.value = Resource.Loading()
            _kycSubmitState.value = submitUserKycUseCase(
                headers, map, panImage, aadharFront, aadharBack, docImage, selfie
            )
        }
    }

    fun resetSubmitState() {
        _kycSubmitState.value = null
    }
}
