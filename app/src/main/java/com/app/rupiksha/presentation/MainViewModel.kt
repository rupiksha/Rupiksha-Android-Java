package com.app.rupiksha.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _errorEvents = MutableSharedFlow<String>()
    val errorEvents: SharedFlow<String> = _errorEvents

    private val _isUserLoggedIn = MutableStateFlow(storageUtil.accessToken.isNotEmpty())
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn

    fun showError(message: String) {
        viewModelScope.launch {
            _errorEvents.emit(message)
        }
    }

    fun updateLoginStatus() {
        _isUserLoggedIn.value = storageUtil.accessToken.isNotEmpty()
    }

    fun logout() {
        storageUtil.clearAll()
        updateLoginStatus()
    }
}
