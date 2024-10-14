package org.bmach01.ackey.ui.viewmodel

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.bmach01.ackey.data.repo.AccessKeyRepo
import org.bmach01.ackey.data.repo.AuthenticationRepo
import org.bmach01.ackey.data.repo.SecretRepo
import org.bmach01.ackey.data.source.HttpClientProvider
import org.bmach01.ackey.domain.CodeGenerator
import org.bmach01.ackey.domain.TokenRefreshUseCase
import org.bmach01.ackey.ui.AppScreen
import org.bmach01.ackey.ui.state.KeyState
import java.net.ConnectException

class KeyViewModel(
    private val navigateTo: (String) -> Unit,
    context: Context
): ViewModel() {

    private val codeGenerator = CodeGenerator()
    private val _uiState = MutableStateFlow(KeyState())
    val uiState: StateFlow<KeyState> = _uiState.asStateFlow()

    private val secretRepo = SecretRepo(context)
    private val accessKeyRepo = AccessKeyRepo(getToken = secretRepo::getToken)
    private val authenticationRepo = AuthenticationRepo(getToken = secretRepo::getToken)

    private val refreshToken = TokenRefreshUseCase(secretRepo, authenticationRepo)::refresh
    private val handler: Handler = Handler(Looper.getMainLooper())

    init {
        // TODO DELETE THIS | DEBUG ONLY
        viewModelScope.launch {
            HttpClientProvider.serverUrl = "http://10.0.2.2:8080"
            val login = "JohnDoe"
            val password = "kiud#9ru7nbJgaC%kJJ9G2Q@7%ok2z9%up4F\$C%kSVpAXEq1AAbLxn11azZD4M6frzM3Sx4&l\$G@6x!XDjKFuDATszS%ECc7LoP#Jxtg5KPQ#awtrUBd8SDpdT%5G6EI"
            secretRepo.saveLogin(login)
            secretRepo.savePassword(password)

            Log.d("bmach", "${Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())}")
        }

        // TODO clean it up when done (this is just for preview WIP)
        _uiState.update { it.copy(bitmap =
            codeGenerator.generateQRCode(
                format = BarcodeFormat.CODE_128,
                content = uiState.value.key ?: "",
                width = 1024,
                height = 256
            ).asImageBitmap()
        ) }

        onRefresh()
    }

    fun onRefresh() {
        viewModelScope.launch {
            handler.removeCallbacks(::onRefresh)

            try {
                val key = accessKeyRepo.getAccessKey()
                _uiState.update { it.copy(
                    key = key.key,
                    validUntil = key.validUntil
                ) }
            }
            catch (e: ConnectException) {
                Log.d("bmach", "connection error")
                _uiState.update { it.copy(error = "Connection error") }
            }
            catch (e: ClientRequestException) {
                Log.d("bmach", "403 error")
                try {
                    refreshToken()
                    val key = accessKeyRepo.getAccessKey()
                    _uiState.update { it.copy(
                        key = key.key,
                        validUntil = key.validUntil
                    ) }
                }
                catch (e: Exception) {
                    _uiState.update { it.copy(error = "Account error") }
                }
            }
            if (uiState.value.validUntil != null)
                handler.postDelayed(::onRefresh,
                    uiState.value.validUntil!!
                        .minus(Clock.System.now())
                        .inWholeMilliseconds
                )
        }
    }

    fun navigateToSettings() {
        navigateTo(AppScreen.SettingsScreen.name)
    }
}