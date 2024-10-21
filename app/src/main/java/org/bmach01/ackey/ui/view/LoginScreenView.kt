package org.bmach01.ackey.ui.view

import org.bmach01.ackey.R
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
    val context = LocalContext.current

    // TODO: somehow add logic for cancel biometric login (go to registration from biometric login)
    if (viewmodel.isBiometricHelperInitialized && uiState.authenticationMethod == AuthenticationMethod.SYSTEM)
        LaunchedEffect(Unit) {
            viewmodel.showBiometricPrompt(
                context.resources.getString(R.string.system_login_to),
                context.resources.getString(R.string.system_login_description)
            )
        }

    else
        PINKeyboardView(
            title = stringResource(R.string.app_login),
            confirming = true,
            pin = "",
            pin2 = uiState.pin,
            instructions = stringResource(uiState.error),
            onCancel = navigateToRegistration,
            onChange = viewmodel::onChangePIN,
            onSubmit = viewmodel::onSubmit
        )

    if (uiState.authenticated)
        LaunchedEffect(Unit) {
            navigateToKey()
        }
}