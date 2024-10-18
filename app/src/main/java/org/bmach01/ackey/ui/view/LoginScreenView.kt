package org.bmach01.ackey.ui.view

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import org.bmach01.ackey.data.model.AuthenticationMethod
import org.bmach01.ackey.ui.viewmodel.LoginViewModel

@Composable
fun MainLoginScreenView(
    navigateToKey: () -> Unit,
    navigateToRegistration: () -> Unit,
    viewmodel: LoginViewModel = hiltViewModel()
) {
    BackHandler(true) {}
    val uiState = viewmodel.uiState.collectAsState().value

    // TODO: somehow add logic for cancel biometric login (go to registration from biometric login)
    if (viewmodel.isBiometricHelperInitialized && uiState.authenticationMethod == AuthenticationMethod.SYSTEM)
        LaunchedEffect(Unit) {
            viewmodel.showBiometricPrompt("Login", "Lorem ipsum")
        }

    else
        PINKeyboardView(
            title = "AcKey Login",
            confirming = true,
            pin = "",
            pin2 = uiState.pin,
            instructions = uiState.error,
            onCancel = navigateToRegistration,
            onChange = viewmodel::onChangePIN,
            onSubmit = viewmodel::onSubmit
        )

    if (uiState.authenticated)
        LaunchedEffect(Unit) {
            navigateToKey()
        }
}