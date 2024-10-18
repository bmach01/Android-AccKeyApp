package org.bmach01.ackey.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.bmach01.ackey.data.repo.SecretRepo
import org.bmach01.ackey.data.repo.SettingsRepo
import org.bmach01.ackey.ui.AppScreen
import javax.inject.Inject

@HiltViewModel
class AcKeyAppViewModel @Inject constructor(
    private val secretRepo: SecretRepo,
    private val settingsRepo: SettingsRepo
): ViewModel() {

    fun getInitialScreen(): AppScreen {
        return runBlocking {
            val urlDeferred = async { settingsRepo.getServerBaseUrl() }
            val usernameDeferred = async { secretRepo.getLogin() }
            val passwordDeferred = async { secretRepo.getPassword() }

            val url = urlDeferred.await()
            val username = usernameDeferred.await()
            val password = passwordDeferred.await()

            if (url.isEmpty() || username.isEmpty() || password.isEmpty()) {
                return@runBlocking AppScreen.RegisterScreen
            }
            return@runBlocking AppScreen.LoginScreen
        }
    }

}