package com.app.rupiksha.presentation.recharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.recharge.DoRechargeUseCase
import com.app.rupiksha.domain.use_case.recharge.FetchOperatorUseCase
import com.app.rupiksha.domain.use_case.recharge.GetOperatorsUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.AllPlans
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.PlanDataModel
import com.app.rupiksha.models.RechargeOperatorModel
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class RechargeViewModel @Inject constructor(
    private val getOperatorsUseCase: GetOperatorsUseCase,
    private val fetchOperatorUseCase: FetchOperatorUseCase,
    private val doRechargeUseCase: DoRechargeUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _operatorsState = MutableStateFlow<Resource<List<RechargeOperatorModel>>?>(null)
    val operatorsState: StateFlow<Resource<List<RechargeOperatorModel>>?> = _operatorsState

    private val _fetchOperatorState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val fetchOperatorState: StateFlow<Resource<BaseResponse>?> = _fetchOperatorState

    private val _rechargeState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val rechargeState: StateFlow<Resource<BaseResponse>?> = _rechargeState

    private val _plansState = MutableStateFlow<List<AllPlans>>(emptyList())
    val plansState: StateFlow<List<AllPlans>> = _plansState

    private val _planTitlesState = MutableStateFlow<List<PlanDataModel>>(emptyList())
    val planTitlesState: StateFlow<List<PlanDataModel>> = _planTitlesState

    init {
        getOperators()
    }

    fun getOperators() {
        val headers = getHeaders()
        viewModelScope.launch {
            _operatorsState.value = Resource.Loading()
            val result = getOperatorsUseCase(headers)
            if (result is Resource.Success) {
                _operatorsState.value =
                    Resource.Success(result.data?.data?.operatorList ?: emptyList())
            } else {
                _operatorsState.value =
                    Resource.Error(result.message ?: "Failed to fetch operators")
            }
        }
    }

    fun fetchOperator(mobile: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("mobile", mobile)
            .build()

        viewModelScope.launch {
            _fetchOperatorState.value = Resource.Loading()
            val result = fetchOperatorUseCase(headers, requestBody)
            _fetchOperatorState.value = result
            if (result is Resource.Success) {
                _plansState.value = result.data?.data?.allPlans ?: emptyList()
                _planTitlesState.value = result.data?.data?.planTitle ?: emptyList()
            }
        }
    }

    fun doRecharge(mobile: String, operatorId: Int, amount: String) {
        val headers = getHeaders()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("mobile", mobile)
            .addFormDataPart("operator", operatorId.toString())
            .addFormDataPart("amount", amount)
            .build()

        viewModelScope.launch {
            _rechargeState.value = Resource.Loading()
            _rechargeState.value = doRechargeUseCase(headers, requestBody)
        }
    }

    private fun getHeaders() = mapOf(
        "headerToken" to (storageUtil.getAccessToken() ?: ""),
        "headerKey" to storageUtil.getApiKey()
    )

    fun resetRechargeState() {
        _rechargeState.value = null
    }

    fun resetFetchOperatorState() {
        _fetchOperatorState.value = null
        _plansState.value = emptyList()
        _planTitlesState.value = emptyList()
    }
}
