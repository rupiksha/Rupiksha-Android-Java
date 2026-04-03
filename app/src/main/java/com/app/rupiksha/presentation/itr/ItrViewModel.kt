package com.app.rupiksha.presentation.itr

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.itr.SubmitItrFormUseCase
import com.app.rupiksha.domain.use_case.itr.UploadItrImageUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BankDetailModel
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.TaxFormDetailModel
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
class ItrViewModel @Inject constructor(
    private val uploadItrImageUseCase: UploadItrImageUseCase,
    private val submitItrFormUseCase: SubmitItrFormUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _uploadState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val uploadState: StateFlow<Resource<BaseResponse>?> = _uploadState

    private val _submitState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val submitState: StateFlow<Resource<BaseResponse>?> = _submitState

    // Form data states
    val investmentDetailList = MutableStateFlow<List<TaxFormDetailModel>>(emptyList())
    val otherIncomeList = MutableStateFlow<List<TaxFormDetailModel>>(emptyList())
    val otherExpensesList = MutableStateFlow<List<TaxFormDetailModel>>(emptyList())
    val bankDetailList = MutableStateFlow<List<BankDetailModel>>(emptyList())

    fun addInvestmentItem() {
        val current = investmentDetailList.value.toMutableList()
        current.add(TaxFormDetailModel(id = current.size + 1, isOtherDocument = true))
        investmentDetailList.value = current
    }

    fun addOtherIncomeItem() {
        val current = otherIncomeList.value.toMutableList()
        current.add(TaxFormDetailModel(id = current.size + 1, isOtherDocument = true))
        otherIncomeList.value = current
    }

    fun addOtherExpenseItem() {
        val current = otherExpensesList.value.toMutableList()
        current.add(TaxFormDetailModel(id = current.size + 1, isOtherDocument = true))
        otherExpensesList.value = current
    }

    fun addBankDetailItem() {
        val current = bankDetailList.value.toMutableList()
        current.add(BankDetailModel(id = current.size + 1, isOtherDocument = true))
        bankDetailList.value = current
    }

    fun uploadImage(filePart: MultipartBody.Part?, onResult: (String) -> Unit) {
        val headers = mapOf(
            "api-key" to storageUtil.accessToken,
            "api-secret" to storageUtil.apiKey,
            "appversion" to "1.0" // Replace with actual version
        )
        val partMap = mapOf("file_type" to "Image".toRequestBody("multipart/form-data".toMediaTypeOrNull()))

        viewModelScope.launch {
            _uploadState.value = Resource.Loading()
            val result = uploadItrImageUseCase(partMap, headers, filePart)
            _uploadState.value = result
            if (result is Resource.Success) {
                result.data?.data?.fileName?.let { onResult(it) }
            }
        }
    }

    fun submitForm(partMap: Map<String, RequestBody>) {
        val headers = mapOf(
            "api-key" to storageUtil.accessToken,
            "api-secret" to storageUtil.apiKey,
            "appversion" to "1.0"
        )

        viewModelScope.launch {
            _submitState.value = Resource.Loading()
            _submitState.value = submitItrFormUseCase(partMap, headers)
        }
    }

    fun resetStates() {
        _uploadState.value = null
        _submitState.value = null
    }
}
