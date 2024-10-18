package org.bmach01.ackey.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bmach01.ackey.data.model.AuthenticationMethod
import org.bmach01.ackey.data.repo.SecretRepo
import org.bmach01.ackey.data.repo.SettingsRepo
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo,
    private val secretRepo: SecretRepo
): ViewModel() {

    private val _authenticationMethod = MutableStateFlow<AuthenticationMethod>(AuthenticationMethod.PIN)
    val authenticationMethod: StateFlow<AuthenticationMethod> = _authenticationMethod.asStateFlow()

    private fun updateAuthenticationMethod() {
        viewModelScope.launch {
            _authenticationMethod.update { settingsRepo.getAuthenticationMethod() }
        }
    }

    init {
        updateAuthenticationMethod()
    }

}