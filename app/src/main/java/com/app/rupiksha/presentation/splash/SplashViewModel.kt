package com.app.rupiksha.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rupiksha.presentation.navigation.Screen
import com.app.rupiksha.storage.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val storageUtil: StorageUtil
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<Screen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        startSplashScreen()
    }

    private fun startSplashScreen() {
        viewModelScope.launch {
            delay(2500)
            if (storageUtil.getUserInfo() != null) {
                _navigationEvent.emit(Screen.Home)
            } else {
                _navigationEvent.emit(Screen.Intro)
            }
        }
    }
}