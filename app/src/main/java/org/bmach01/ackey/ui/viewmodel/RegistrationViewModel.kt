package org.bmach01.ackey.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.bmach01.ackey.ui.state.RegistrationState

class RegistrationViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationState())
    val uiState: StateFlow<RegistrationState> = _uiState.asStateFlow()

    fun onUrlChange(url: String) {
        _uiState.update { it.copy(url = url, urlError = "") }
    }

    fun onOTPChange(otp: String) {
        _uiState.update { it.copy(otp = otp, otpError = "") }
    }

    fun onSubmit() {
        _uiState.update { it.copy(inputUnlocked = false) }
        // TODO: send data and get errors back
        _uiState.update { it.copy(inputUnlocked = true) }
    }
}