package org.bmach01.ackey.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bmach01.ackey.data.model.Credentials
import org.bmach01.ackey.data.repo.AuthenticationRepo
import org.bmach01.ackey.data.repo.SecretRepo
import org.bmach01.ackey.data.source.HttpClientProvider
import org.bmach01.ackey.ui.AppScreen
import org.bmach01.ackey.ui.state.RegistrationState
import java.net.ConnectException

class RegistrationViewModel(
    private val navigateTo: (String) -> Unit,
    context: Context
): ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationState())
    val uiState: StateFlow<RegistrationState> = _uiState.asStateFlow()

    private val secretRepo = SecretRepo(context)
    private val authenticationRepo = AuthenticationRepo(getToken = secretRepo::getToken)

    fun onUrlChange(url: String) {
        _uiState.update { it.copy(url = url, urlError = "") }
    }

    fun onOTPChange(otp: String) {
        _uiState.update { it.copy(otp = otp, otpError = "") }
    }

    fun onSubmit() {
        _uiState.update { it.copy(inputUnlocked = false) }
        // TODO: send data and get errors back

        viewModelScope.launch {
            HttpClientProvider.serverUrl = uiState.value.url

            val credentials: Credentials

            try {
                credentials = authenticationRepo.register(uiState.value.otp)
            }
            catch (e: ConnectException) {
                // TODO: add string resources
                _uiState.update { it.copy(urlError = "Cannot connect to the server") }
                return@launch
            }
            catch (e: ClientRequestException) {
                // TODO: add string resources
                _uiState.update { it.copy(otpError = "Invalid OTP provided or the account has already been activated. Contact your system administrator.") }
                return@launch
            }

            secretRepo.saveLogin(credentials.username)
            secretRepo.savePassword(credentials.password)

            val token: String

            try {
                token = authenticationRepo.login(credentials)
            }
            catch (e: ConnectException) {
                // TODO: add string resources
                _uiState.update { it.copy(urlError = "Cannot connect to the server") }
                return@launch
            }
            catch (e: ClientRequestException) {
                // TODO: add string resources
                _uiState.update { it.copy(otpError = "Internal system error. Try again later or contact your system administrator.") }
                return@launch
            }

            secretRepo.saveToken(token)

            navigateTo(AppScreen.PINKeyboardScreen.name)
        }

        _uiState.update { it.copy(inputUnlocked = true) }
    }

    fun navigateToPINKeyboard() {
        _uiState.update { it.copy(navigation = AppScreen.PINKeyboardScreen) }
    }
}