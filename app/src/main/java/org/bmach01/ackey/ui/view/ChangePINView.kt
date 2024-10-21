package org.bmach01.ackey.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import org.bmach01.ackey.ui.viewmodel.ChangePINViewModel

@Composable
fun MainChangePINView(
    goBack: () -> Unit,
    viewmodel: ChangePINViewModel = hiltViewModel()
) {
    val uiState = viewmodel.uiState.collectAsState().value

    PINKeyboardView(
        title = stringResource(uiState.title),
        confirming = uiState.confirming,
        pin = uiState.pin,
        pin2 = uiState.pin2,
        instructions = stringResource(uiState.instructions),
        onCancel = viewmodel::onCancel,
        onChange = viewmodel::onChangePIN,
        onSubmit = viewmodel::onSubmit
    )

    if (uiState.navigation)
        LaunchedEffect(Unit) {
            goBack()
        }

}