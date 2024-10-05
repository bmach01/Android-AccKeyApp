package org.bmach01.ackey.ui.viewmodel

import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.bmach01.ackey.domain.CodeGenerator
import org.bmach01.ackey.ui.AppScreen
import org.bmach01.ackey.ui.state.KeyState

class KeyViewModel(
    private val navigateTo: (String) -> Unit
): ViewModel() {

    // TODO switch it up for model that holds the codegenerator reference
    private val codeGenerator = CodeGenerator()
    private val _uiState = MutableStateFlow(KeyState())
    val uiState: StateFlow<KeyState> = _uiState.asStateFlow()

    init {
        // TODO clean it up when done (this is just for preview WIP)
        _uiState.update { it.copy(bitmap =
            codeGenerator.generateQRCode(
                format = BarcodeFormat.CODE_128,
                content = uiState.value.data,
                width = 1024,
                height = 256
            ).asImageBitmap()
        ) }
    }

    fun onRefresh() {
        // TODO fetch new code and present it
    }

    fun navigateToSettings() {
        navigateTo(AppScreen.SettingsScreen.name)
    }
}