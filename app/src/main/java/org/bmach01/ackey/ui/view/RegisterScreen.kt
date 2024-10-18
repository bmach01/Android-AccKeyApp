package org.bmach01.ackey.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.bmach01.ackey.ui.viewmodel.RegistrationViewModel

@Preview
@Composable
fun MainRegisterPreview() {
    MainRegisterView({})
}

@Composable
fun MainRegisterView(
    navigateToLoginSetup: () -> Unit,
    viewmodel: RegistrationViewModel = hiltViewModel()
) {
    val uiState = viewmodel.uiState.collectAsState().value

    if (uiState.navigation)
        LaunchedEffect(Unit) {
            navigateToLoginSetup()
        }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 128.dp)
    ) {
        val inputModifier = Modifier
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .fillMaxWidth()

        val titleModifier = Modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth(

            )
        Text(
            text = "Device Registration",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = titleModifier
        )
        Text(
            text = "Powered by AcKey",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
            modifier = titleModifier
        )

        // URL input
        RegisterInput(
            onChange = viewmodel::onUrlChange,
            value = uiState.url,
            error = uiState.urlError,
            enabled = uiState.inputUnlocked,
            label = "Domain URL",
            placeholder = "www.domain.com",
            modifier = inputModifier
        )


        // OTP input
        RegisterInput(
            onChange = viewmodel::onOTPChange,
            value = uiState.otp,
            error = uiState.otpError,
            enabled = uiState.inputUnlocked,
            label = "One Time Password",
            placeholder = "Password...",
            modifier = inputModifier
        )

        Spacer(modifier = Modifier.height(128.dp))

        FilledTonalIconButton(
            onClick = viewmodel::onSubmit,
            enabled = uiState.inputUnlocked,
            shape = RoundedCornerShape(50f),
            modifier = Modifier
                .size(100.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Create,
                contentDescription = "Register button",
                modifier = Modifier.size(50.dp)
            )
        }
    }
}

@Composable
fun RegisterInput(
    onChange: (String) -> Unit,
    value: String,
    enabled: Boolean,
    label: String,
    error: String = "",
    placeholder: String,
    modifier: Modifier,
) {
    val isError = error.isNotEmpty()
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        enabled = enabled,
        singleLine = true,
        modifier = modifier,
        isError = isError,
        supportingText = {
            if (isError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = {
            if (isError)
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error
                )
        },
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        label = {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }
    )

}