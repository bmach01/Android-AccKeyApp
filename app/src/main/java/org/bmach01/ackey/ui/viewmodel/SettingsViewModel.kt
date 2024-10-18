package org.bmach01.ackey.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bmach01.ackey.data.model.AuthenticationMethod
import org.bmach01.ackey.data.model.Credentials
import org.bmach01.ackey.data.repo.AuthenticationRepo
import org.bmach01.ackey.data.repo.SecretRepo
import org.bmach01.ackey.data.repo.SettingsRepo
import org.bmach01.ackey.data.repo.UserManagementRepo
import org.bmach01.ackey.domain.TokenRefreshUseCase
import org.bmach01.ackey.ui.state.SettingsState
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo,
    private val secretRepo: SecretRepo,
    private val userManagementRepo: UserManagementRepo,
    private val authenticationRepo: AuthenticationRepo
): ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    private val refreshToken = TokenRefreshUseCase(secretRepo, authenticationRepo)::refresh

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
        }
    }

    fun onDeactivate() {
        viewModelScope.launch {
            val usernameD = async { secretRepo.getLogin() }
            val passwordD = async { secretRepo.getPassword() }
            val tokenD = async { secretRepo.getToken() }

            val (username, password, token) = awaitAll(usernameD, passwordD, tokenD)
            val credentials = Credentials(username, password)
            var deactivated: Boolean = false

            try {
                deactivated = userManagementRepo.deactivateAccount(
                    credentials = credentials,
                    token = token
                )
            }
            catch (e: ConnectException) {
                // TODO: failure popup
            }
            catch (e: ClientRequestException) {
                try {
                    refreshToken()
                    deactivated = userManagementRepo.deactivateAccount(
                        credentials = credentials,
                        token = token
                    )
                }
                catch (e: Exception) {
                    Log.d("bmach", "settings::deactivate ClientRequestException caught!")
                }
            }

            if (deactivated) {
                settingsRepo.clearAllLocalData()
                _uiState.update { it.copy(navigateToRegistration = true) }
                return@launch
            }

            // TODO: failure popup
        }
    }
}