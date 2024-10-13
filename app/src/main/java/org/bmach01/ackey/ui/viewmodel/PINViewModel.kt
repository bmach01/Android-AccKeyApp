package org.bmach01.ackey.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bmach01.ackey.data.model.AuthenticationMethod
import org.bmach01.ackey.data.repo.SecretRepo
import org.bmach01.ackey.data.repo.SettingsRepo
import org.bmach01.ackey.domain.BiometricHelper
import org.bmach01.ackey.ui.AppScreen
import org.bmach01.ackey.ui.state.PINState

class PINViewModel(
    private val navigateTo: (String) -> Unit,
    context: Context
): ViewModel() {
    private val _uiState = MutableStateFlow(PINState())
    val uiState: StateFlow<PINState> = _uiState.asStateFlow()

    private val secretRepo = SecretRepo(context)
    private val settingsRepo = SettingsRepo(context)

    private lateinit var biometricHelper: BiometricHelper
    var isBiometricHelperInitialized = true
        private set

    private lateinit var biometricResult: StateFlow<BiometricHelper.BiometricResult?>

    init {
        try {
            biometricHelper = BiometricHelper()
            biometricResult = biometricHelper.resultChannel
        }
        catch (e: InstantiationException) {
            Log.d(this.javaClass.name, e.message?: "Instantiation exception caught!")
            isBiometricHelperInitialized = false
        }
    }

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
            _uiState.update { it.copy(isBiometricSetupOpen = true) }
        }

    }

    fun onCancel() {
        _uiState.update { PINState() }
    }

    fun showBiometricPrompt(title: String, description: String) {
        biometricHelper.showBiometricPrompt(title, description)
    }

    fun onBiometricSetupResult(result: ActivityResult) {
        viewModelScope.launch {
            settingsRepo.saveAuthenticationMethod(AuthenticationMethod.SYSTEM)
        }
        navigateTo(AppScreen.KeyScreen.name)
    }

    fun onBiometricSetupCancel() {
        viewModelScope.launch {
            settingsRepo.saveAuthenticationMethod(AuthenticationMethod.PIN)
        }
        navigateTo(AppScreen.KeyScreen.name)
    }

    fun onBiometricSetupAccept(
        enrollLauncher: ActivityResultLauncher<Intent>
    ) {
        if (biometricResult.value is BiometricHelper.BiometricResult.AuthenticationNotSet) {
            if(Build.VERSION.SDK_INT >= 30) {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                enrollLauncher.launch(enrollIntent)
            }
        }
        else {
            showBiometricPrompt("Confirm your identity for AcKey", "You can disable this method of authentication in the settings")
            viewModelScope.launch {
                biometricResult.collect {
                    if (it is BiometricHelper.BiometricResult.AuthenticationSuccess) {
                        navigateTo(AppScreen.KeyScreen.name)
                        return@collect
                    }
                }
            }
        }
    }

}