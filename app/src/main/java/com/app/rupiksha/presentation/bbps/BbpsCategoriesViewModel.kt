package com.app.rupiksha.presentation.bbps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.domain.use_case.bbps.GetBbpsCategoriesUseCase
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BbpsServiceModel
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BbpsCategoriesViewModel @Inject constructor(
    private val getBbpsCategoriesUseCase: GetBbpsCategoriesUseCase,
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _categoriesState = MutableStateFlow<Resource<List<BbpsServiceModel>>?>(null)
    val categoriesState: StateFlow<Resource<List<BbpsServiceModel>>?> = _categoriesState

    init {
        getCategories()
    }

    fun getCategories() {
        val headers = mapOf(
            "headerToken" to storageUtil.accessToken,
            "headerKey" to storageUtil.apiKey
        )
        viewModelScope.launch {
            _categoriesState.value = Resource.Loading()
            val result = getBbpsCategoriesUseCase(headers)
            if (result is Resource.Success) {
                _categoriesState.value = Resource.Success(result.data?.data?.bbps ?: emptyList())
            } else {
                _categoriesState.value = Resource.Error(result.message ?: "Failed to load categories")
            }
        }
    }
}
