package org.bmach01.ackey.ui.viewmodel

import org.bmach01.ackey.R
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
        title = R.string.input_new_pin,
        instructions = R.string.emptyString,

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
            _uiState.update { it.copy(confirming = true, title = R.string.confirm_pin) }
            return
        }

        if (uiState.value.pin != uiState.value.pin2) {
            _uiState.update { it.copy(instructions = R.string.pins_not_matching_instruction) }
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