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
import kotlinx.coroutines.runBlocking
import org.bmach01.ackey.data.repo.SecretRepo
import org.bmach01.ackey.data.repo.SettingsRepo
import org.bmach01.ackey.domain.BiometricHelper
import org.bmach01.ackey.ui.state.LoginState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo,
    private val secretRepo: SecretRepo
): ViewModel() {
    private lateinit var biometricHelper: BiometricHelper
    var isBiometricHelperInitialized = true
        private set
    private lateinit var biometricResult: StateFlow<BiometricHelper.BiometricResult?>

    private val _uiState = MutableStateFlow<LoginState>(LoginState())
    val uiState = _uiState.asStateFlow()

    init {
        initBiometricHelper()
        updateAuthenticationMethod()

    }

    private fun initBiometricHelper() {
        try {
            biometricHelper = BiometricHelper()
            biometricResult = biometricHelper.resultChannel
        }
        catch (e: InstantiationException) {
            Log.d(this.javaClass.name, e.message?: "Instantiation exception caught!")
            isBiometricHelperInitialized = false
        }

    }

    private fun updateAuthenticationMethod() {
        runBlocking {
            if (!isBiometricHelperInitialized) return@runBlocking
            _uiState.update { it.copy(authenticationMethod = settingsRepo.getAuthenticationMethod()) }
        }
    }


    fun showBiometricPrompt(title: String, description: String) {
        biometricHelper.showBiometricPrompt(title, description)
    }

    fun onChangePIN(pin: String) {
        _uiState.update { it.copy(pin = pin) }
    }

    fun onSubmit() {
        viewModelScope.launch {
            if (uiState.value.pin != secretRepo.getPIN()) {
                _uiState.update { it.copy(error = "Incorrect PIN") }
                return@launch
            }

            _uiState.update { it.copy(authenticated = true) }
        }
    }

}