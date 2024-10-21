package org.bmach01.ackey.ui.viewmodel

import org.bmach01.ackey.R
import android.os.Handler
import android.os.Looper
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
import org.bmach01.ackey.domain.CodeGenerator
import org.bmach01.ackey.domain.TokenRefreshUseCase
import org.bmach01.ackey.ui.state.KeyState
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class KeyViewModel @Inject constructor(
    private val secretRepo: SecretRepo,
    private val accessKeyRepo: AccessKeyRepo,
    private val authenticationRepo: AuthenticationRepo,
): ViewModel() {

    private val codeGenerator = CodeGenerator()
    private val _uiState = MutableStateFlow(KeyState())
    val uiState: StateFlow<KeyState> = _uiState.asStateFlow()

    private val refreshToken = TokenRefreshUseCase(secretRepo, authenticationRepo)::refresh

    private val handler: Handler = Handler(Looper.getMainLooper())

    init {
        onRefresh()
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
                    error = R.string.connection_error) }
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
                        error = R.string.account_error) }
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
}