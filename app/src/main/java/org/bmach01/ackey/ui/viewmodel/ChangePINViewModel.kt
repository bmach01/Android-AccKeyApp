package org.bmach01.ackey.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bmach01.ackey.data.repo.SecretRepo
import org.bmach01.ackey.ui.state.LoginSetupState
import javax.inject.Inject

@HiltViewModel
class ChangePINViewModel @Inject constructor(
    private val secretRepo: SecretRepo
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginSetupState(
        title = "Input new PIN",
        instructions = "",

    ))
    val uiState: StateFlow<LoginSetupState> = _uiState.asStateFlow()

    fun onChangePIN(pin: String) {
        if (!uiState.value.confirming)
            _uiState.update { it.copy(pin = pin) }
        else
            _uiState.update { it.copy(pin2 = pin) }
    }

    fun onSubmit() {
        if (!uiState.value.confirming) {
            _uiState.update { it.copy(confirming = true, title = "Confirm PIN") }
            return
        }

        if (uiState.value.pin != uiState.value.pin2) {
            _uiState.update { it.copy(instructions = "Current input is not matching previously set PIN") }
            return
        }

        viewModelScope.launch {
            secretRepo.savePIN(uiState.value.pin)
            _uiState.update { it.copy(navigation = true) }
        }

    }

    fun onCancel() {
        _uiState.update { LoginSetupState() }
    }

}