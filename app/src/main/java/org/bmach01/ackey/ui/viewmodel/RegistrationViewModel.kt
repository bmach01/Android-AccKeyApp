package org.bmach01.ackey.ui.viewmodel

import org.bmach01.ackey.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bmach01.ackey.data.model.Credentials
import org.bmach01.ackey.data.repo.AuthenticationRepo
import org.bmach01.ackey.data.repo.SecretRepo
import org.bmach01.ackey.data.repo.SettingsRepo
import org.bmach01.ackey.ui.state.RegistrationState
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val secretRepo: SecretRepo,
    private val authenticationRepo: AuthenticationRepo,
    private val settingsRepo: SettingsRepo
): ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationState())
    val uiState: StateFlow<RegistrationState> = _uiState.asStateFlow()

    fun onUrlChange(url: String) {
        _uiState.update { it.copy(url = url, urlError = R.string.emptyString) }
    }

    fun onOTPChange(otp: String) {
        _uiState.update { it.copy(otp = otp, otpError = R.string.emptyString) }
    }

    fun onSubmit() {
        _uiState.update { it.copy(inputUnlocked = false) }

        viewModelScope.launch {
            settingsRepo.saveServerBaseUrl(uiState.value.url)

            val credentials: Credentials

            try {
                credentials = authenticationRepo.register(uiState.value.otp)
            }
            catch (e: ConnectException) {
                _uiState.update { it.copy(urlError = R.string.cannot_connect) }
                return@launch
            }
            catch (e: ClientRequestException) {
                _uiState.update { it.copy(otpError = R.string.otp_error) }
                return@launch
            }

            secretRepo.saveLogin(credentials.username)
            secretRepo.savePassword(credentials.password)

            val token: String

            try {
                token = authenticationRepo.login(credentials)
            }
            catch (e: ConnectException) {
                _uiState.update { it.copy(urlError =R.string.cannot_connect) }
                return@launch
            }
            catch (e: ClientRequestException) {
                _uiState.update { it.copy(otpError = R.string.internal_error) }
                return@launch
            }

            secretRepo.saveToken(token)

            navigateToPINKeyboard()
        }

        _uiState.update { it.copy(inputUnlocked = true) }
    }

    private fun navigateToPINKeyboard() {
        _uiState.update { it.copy(navigation = true) }
    }
}