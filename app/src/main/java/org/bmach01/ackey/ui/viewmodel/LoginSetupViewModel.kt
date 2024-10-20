package org.bmach01.ackey.ui.viewmodel

import android.content.Context
import org.bmach01.ackey.R
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
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bmach01.ackey.data.model.AuthenticationMethod
import org.bmach01.ackey.data.repo.SecretRepo
import org.bmach01.ackey.data.repo.SettingsRepo
import org.bmach01.ackey.domain.BiometricHelper
import org.bmach01.ackey.ui.state.BiometricSetupState
import org.bmach01.ackey.ui.state.LoginSetupState
import javax.inject.Inject

@HiltViewModel
class LoginSetupViewModel @Inject constructor(
    private val secretRepo: SecretRepo,
    private val settingsRepo: SettingsRepo,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val _uiPINState = MutableStateFlow(LoginSetupState())
    val uiPINState = _uiPINState.asStateFlow()
    private val _uiBiometricState = MutableStateFlow(BiometricSetupState())
    val uiBiometricState = _uiBiometricState.asStateFlow()

    private lateinit var biometricHelper: BiometricHelper
    var isBiometricHelperInitialized = true
        private set

    private lateinit var biometricResult: StateFlow<BiometricHelper.BiometricResult?>

    init {
        initBiometricHelper()
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

    fun onChangePIN(pin: String) {
        if (!uiPINState.value.confirming)
            _uiPINState.update { it.copy(pin = pin) }
        else
            _uiPINState.update { it.copy(pin2 = pin) }
    }

    fun onSubmit() {
        if (!uiPINState.value.confirming) {
            _uiPINState.update { it.copy(confirming = true, title = R.string.confirm_pin) }
            return
        }

        if (uiPINState.value.pin != uiPINState.value.pin2) {
            _uiPINState.update { it.copy(instructions = R.string.pins_not_matching_instruction) }
            return
        }

        viewModelScope.launch {
            secretRepo.savePIN(uiPINState.value.pin)
            _uiBiometricState.update { it.copy(isBiometricSetupOpen = true) }
        }

    }

    fun onCancel() {
        _uiPINState.update { LoginSetupState() }
        _uiBiometricState.update { BiometricSetupState() }
    }

    fun showBiometricPrompt(title: String, description: String) = biometricHelper.showBiometricPrompt(title, description)


    fun onBiometricSetupResult(result: ActivityResult) {
        viewModelScope.launch {
            settingsRepo.saveAuthenticationMethod(AuthenticationMethod.SYSTEM)
        }
        navigateToKey()
    }

    fun onBiometricSetupCancel() {
        viewModelScope.launch {
            settingsRepo.saveAuthenticationMethod(AuthenticationMethod.PIN)
        }
        navigateToKey()
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
            showBiometricPrompt(
                context.resources.getString(R.string.confirm_your_identity_for),
                context.resources.getString(R.string.confirm_your_identity_description)
            )
            viewModelScope.launch {
                biometricResult.collect {
                    if (it is BiometricHelper.BiometricResult.AuthenticationSuccess) {
                        navigateToKey()
                        return@collect
                    }
                }
            }
        }
    }

    fun navigateToKey() {
        _uiPINState.update { it.copy(navigation = true) }
    }

}