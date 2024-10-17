package org.bmach01.ackey.ui.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.bmach01.ackey.data.model.AccessKey
import org.bmach01.ackey.data.repo.AccessKeyRepo
import org.bmach01.ackey.data.repo.AuthenticationRepo
import org.bmach01.ackey.data.repo.SecretRepo
import org.bmach01.ackey.data.repo.SettingsRepo
import org.bmach01.ackey.domain.CodeGenerator
import org.bmach01.ackey.domain.TokenRefreshUseCase
import org.bmach01.ackey.ui.AppScreen
import org.bmach01.ackey.ui.state.KeyState
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class KeyViewModel @Inject constructor(
    private val secretRepo: SecretRepo,
    private val accessKeyRepo: AccessKeyRepo,
    private val authenticationRepo: AuthenticationRepo,
    private val settingsRepo: SettingsRepo // TODO delete this, WIP only
): ViewModel() {

    private val codeGenerator = CodeGenerator()
    private val _uiState = MutableStateFlow(KeyState())
    val uiState: StateFlow<KeyState> = _uiState.asStateFlow()

    private val refreshToken = TokenRefreshUseCase(secretRepo, authenticationRepo)::refresh

    private val handler: Handler = Handler(Looper.getMainLooper())

    init {
        Log.d("bmach", "KeyViewModel initialized")
        // TODO DELETE THIS | DEBUG ONLY
        viewModelScope.launch {
            secretRepo.saveLogin("JohnDoe")
            secretRepo.savePassword("M@b1qiF!31@m9K1PIuEM1!81gdzfJrBzFheDxL3BT5ZLBx&\$Jmh6AGi\$%McmVM&8q0S5f\$7\$aZNU#jo%OyOmdTZMReIJcQ34o6RRMVpW127aTSI!cL9hp0babzdDDC3m")
            settingsRepo.saveServerBaseUrl("http://10.0.2.2:8080")
//            settingsRepo.saveServerBaseUrl("http://192.168.0.102:8080")
            settingsRepo.getServerBaseUrl()
            onRefresh()
        }
    }

    private fun setNewKey(key: AccessKey) {
        _uiState.update { it.copy(
            key = key
        ) }

        _uiState.update { it.copy(bitmap =
            codeGenerator.generateQRCode(
                format = BarcodeFormat.CODE_128,
                content = uiState.value.key?.key ?: "",
                width = 1024,
                height = 256
            ).asImageBitmap()
        ) }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingKey = true) }
            handler.removeCallbacks(::onRefresh)

            try {
                setNewKey(accessKeyRepo.getAccessKey(secretRepo.getToken()))
            }
            catch (e: ConnectException) {
                _uiState.update { it.copy(
                    isLoadingKey = false,
                    error = "Connection error") }
                return@launch
            }
            catch (e: ClientRequestException) {
                try {
                    refreshToken()
                    setNewKey(accessKeyRepo.getAccessKey(secretRepo.getToken()))
                }
                catch (e: Exception) {
                    _uiState.update { it.copy(
                        isLoadingKey = false,
                        error = "Account error") }
                    return@launch
                }
            }
            handler.postDelayed(::onRefresh,
                uiState.value.key!!.validUntil
                    .minus(Clock.System.now())
                    .inWholeMilliseconds
            )

            _uiState.update { it.copy(isLoadingKey = false) }
        }
    }

    fun navigateToSettings() {
        _uiState.update { it.copy(navigation = AppScreen.SettingsScreen) }
    }
}