package org.bmach01.ackey.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bmach01.ackey.data.repo.SecretRepo
import org.bmach01.ackey.ui.state.PINState

class PINViewModel(
    private val navigateTo: (String) -> Unit,
    context: Context
): ViewModel() {
    private val _uiState = MutableStateFlow(PINState())
    val uiState: StateFlow<PINState> = _uiState.asStateFlow()

    private val secretRepo = SecretRepo(context)


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
        }

    }

    fun onCancel() {
        _uiState.update { PINState() }
    }


}