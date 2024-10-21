package org.bmach01.ackey.domain

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BiometricHelper() {

    sealed interface BiometricResult {
        data object HardwareUnavailable: BiometricResult
        data object FeatureUnavailable: BiometricResult
        data class AuthenticationError(val error: String): BiometricResult
        data object AuthenticationFailed: BiometricResult
        data object AuthenticationSuccess: BiometricResult
        data object AuthenticationNotSet: BiometricResult
    }

    companion object {
        private lateinit var manager: BiometricManager

        private lateinit var activity: AppCompatActivity

        fun prepare(activity: AppCompatActivity) {
            this.activity = activity
            manager = BiometricManager.from(activity)
        }
        private fun isReady() = ::manager.isInitialized
    }

    private val authenticators = if(Build.VERSION.SDK_INT >= 30) {
        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
    } else BIOMETRIC_STRONG


    private val _resultChannel = MutableStateFlow<BiometricResult?>(null)
    val resultChannel: StateFlow<BiometricResult?> = _resultChannel.asStateFlow()

    fun canAuthenticate(): Boolean {
        return manager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS
    }

    init {
        if (!isReady())
            throw InstantiationException("BiometricHelper needs to be prepared with BiometricHelper.prepare() first")

        when(manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                _resultChannel.value = BiometricResult.HardwareUnavailable
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                _resultChannel.value = BiometricResult.FeatureUnavailable
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                _resultChannel.value = BiometricResult.AuthenticationNotSet
            }
            else -> Unit
        }
    }

    fun showBiometricPrompt(
        title: String,
        description: String
    ) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setAllowedAuthenticators(authenticators)

        val prompt = BiometricPrompt(
            activity,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    _resultChannel.value = BiometricResult.AuthenticationError(errString.toString())
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    _resultChannel.value = BiometricResult.AuthenticationSuccess
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    _resultChannel.value = BiometricResult.AuthenticationFailed
                }
            }
        )
        prompt.authenticate(promptInfo.build())
    }
}



