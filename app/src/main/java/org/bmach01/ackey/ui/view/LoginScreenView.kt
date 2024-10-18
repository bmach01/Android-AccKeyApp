package org.bmach01.ackey.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import org.bmach01.ackey.data.model.AuthenticationMethod
import org.bmach01.ackey.ui.viewmodel.LoginViewModel

@Composable
fun MainLoginScreenView(
    viewmodel: LoginViewModel = hiltViewModel()
) {
    val authenticationMethod = viewmodel.authenticationMethod.collectAsState().value

    if (authenticationMethod == AuthenticationMethod.PIN)
        PINKeyboardView(
            title = "",
            confirming = false,
            pin = "",
            pin2 = "",
            instructions = "",
            onCancel = {},
            onChange = {},
            onSubmit = {}
        )
    else


}