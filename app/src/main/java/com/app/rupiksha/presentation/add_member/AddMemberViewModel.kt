package com.app.rupiksha.presentation.add_member

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
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AddMemberViewModel @Inject constructor(
    private val getRolesUseCase: GetRolesUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _rolesState = MutableStateFlow<Resource<List<RoleModel>>>(Resource.Loading())
    val rolesState: StateFlow<Resource<List<RoleModel>>> = _rolesState

    private val _addMemberState = MutableStateFlow<Resource<BaseResponse>?>(null)
    val addMemberState: StateFlow<Resource<BaseResponse>?> = _addMemberState

    init {
        getRoles()
    }

    fun getRoles() {
        viewModelScope.launch {
            _rolesState.value = Resource.Loading()
            val result = getRolesUseCase()
            if (result is Resource.Success) {
                _rolesState.value = Resource.Success(result.data?.data?.roles ?: emptyList())
            } else if (result is Resource.Error) {
                _rolesState.value = Resource.Error(result.message ?: "Failed to fetch roles")
            }
        }
    }

    fun addMember(name: String, phone: String, email: String, roleId: Int) {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", name)
            .addFormDataPart("mobile", phone)
            .addFormDataPart("email", email)
            .addFormDataPart("role", roleId.toString())
            .build()

        viewModelScope.launch {
            _addMemberState.value = Resource.Loading()
            _addMemberState.value = registerUseCase(requestBody)
        }
    }

    fun resetState() {
        _addMemberState.value = null
    }
}
