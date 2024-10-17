package org.bmach01.ackey.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bmach01.ackey.data.model.AuthenticationMethod
import org.bmach01.ackey.data.repo.SettingsRepo
import org.bmach01.ackey.ui.state.SettingsState
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo,
): ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            syncSwitches()
        }
    }

    // Only one switch can be set to true
    private suspend fun syncSwitches() {
        val method = settingsRepo.getAuthenticationMethod()

        _uiState.update {
            it.copy(
                systemAuthentication = method == AuthenticationMethod.SYSTEM,
                pinAuthentication = method == AuthenticationMethod.PIN
            )
        }
    }

    fun onSwitch(method: AuthenticationMethod) {
        viewModelScope.launch {
            settingsRepo.saveAuthenticationMethod(method)
            syncSwitches()
            Log.d("bmach", "$method")
        }
    }
}