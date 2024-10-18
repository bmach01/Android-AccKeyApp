package org.bmach01.ackey.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bmach01.ackey.data.repo.SecretRepo
import org.bmach01.ackey.data.repo.SettingsRepo
import org.bmach01.ackey.ui.AppScreen
import javax.inject.Inject

@HiltViewModel
class AcKeyAppViewModel @Inject constructor(
    private val secretRepo: SecretRepo,
    private val settingsRepo: SettingsRepo
): ViewModel() {

    private val _initialScreen = MutableStateFlow<AppScreen>(AppScreen.RegisterScreen)
    val initialScreen: StateFlow<AppScreen> = _initialScreen.asStateFlow()

    private fun updateInitialScreen() {
        viewModelScope.launch {
            val urlDeferred = async { settingsRepo.getServerBaseUrl() }
            val usernameDeferred = async { secretRepo.getLogin() }
            val passwordDeferred = async { secretRepo.getPassword() }

            val url = urlDeferred.await()
            val username = usernameDeferred.await()
            val password = passwordDeferred.await()

//            val username = secretRepo.getLogin()
//            val url = settingsRepo.getServerBaseUrl()
//            val password = secretRepo.getPassword()

            if (url.isEmpty() || username.isEmpty() || password.isEmpty()) {
                return@launch
            }
//            _initialScreen.update { AppScreen.LoginScreen }
        }
    }

    init {
        updateInitialScreen()
    }

}