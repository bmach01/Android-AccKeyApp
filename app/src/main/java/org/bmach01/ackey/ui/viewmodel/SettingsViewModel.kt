package org.bmach01.ackey.ui.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bmach01.ackey.data.AuthenticationMethod
import org.bmach01.ackey.data.LocalSettings
import org.bmach01.ackey.ui.state.RegistrationState
import org.bmach01.ackey.ui.state.SettingsState

class SettingsViewModel(context: Context): ViewModel() {

    private val localSettings = LocalSettings(context)


    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            syncSwitches()
        }
    }

    // Only one switch can be set to true
    private suspend fun syncSwitches() {
        val method = localSettings.authenticationMethod.first()

        Log.d("bmach", "sync switches $method")

        _uiState.update {
            it.copy(
                faceAuthentication = method == AuthenticationMethod.FACE,
                fingerAuthentication = method == AuthenticationMethod.FINGERPRINT,
                passwordAuthentication = method == AuthenticationMethod.PASSWORD
            )
        }
    }

    fun onSwitch(method: AuthenticationMethod) {
        viewModelScope.launch {
            localSettings.saveAuthenticationMethod(method)
            syncSwitches()
            Log.d("bmach", "$method")
        }
    }

}