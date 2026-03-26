package com.app.rupiksha.presentation.login.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.login.GetRolesUseCase
import com.app.rupiksha.domain.use_case.login.RegisterUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.RoleModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val getRolesUseCase: GetRolesUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _rolesState = MutableStateFlow<Resource<List<RoleModel>>?>(null)
    val rolesState: StateFlow<Resource<List<RoleModel>>?> = _rolesState

    private val _registerState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val registerState: StateFlow<Resource<BaseResponse>?> = _registerState

    init {
        getRoles()
    }

    fun getRoles() {
        viewModelScope.launch {
            _rolesState.value = Resource.Loading()
            val result = getRolesUseCase()
            if (result is Resource.Success) {
                _rolesState.value = Resource.Success(result.data?.data?.roles ?: emptyList())
            } else {
                _rolesState.value = Resource.Error(result.message ?: "Failed to fetch roles")
            }
        }
    }

    fun register(name: String, phone: String, email: String, roleId: Int) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", name)
            .addFormDataPart("phone", phone)
            .addFormDataPart("email", email)
            .addFormDataPart("role", roleId.toString())
            .build()

        viewModelScope.launch {
            _registerState.value = Resource.Loading()
            _registerState.value = registerUseCase(requestBody)
        }
    }
    
    fun resetRegisterState() {
        _registerState.value = null
    }
}