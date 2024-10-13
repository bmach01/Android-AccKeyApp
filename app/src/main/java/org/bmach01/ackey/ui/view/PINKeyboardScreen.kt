package org.bmach01.ackey.ui.view

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.bmach01.ackey.domain.BiometricHelper
import org.bmach01.ackey.ui.viewmodel.PINViewModel

@Preview
@Composable
fun MainPINKeyboardPreview() {
    MainLoginView( {} )
}

@Composable
fun MainLoginView(
    navigateTo: (route: String) -> Unit
) {
    val context = LocalContext.current
    val viewmodel = viewModel {
        PINViewModel(
            navigateTo = navigateTo,
            context = context
        )
    }
    val uiState by viewmodel.uiState.collectAsState()

    if (uiState.isBiometricSettingsPrompt and viewmodel.isBiometricHelperInitialized) {
        EnableBiometricsDialogView(
            title = "System authentication",
            description = "Do you want to use authentication system provided by your device (pattern, fingerprint etc.)?",
            onCancel = viewmodel::onBiometricCancel,
            onAccept = viewmodel::onBiometricAccept,
            onResult = viewmodel::onBiometricResult,
        )
    }
    else {
        PINKeyboardView(
            title = uiState.title,
            confirming = uiState.confirming,
            pin = uiState.pin,
            pin2 = uiState.pin2,
            instructions = uiState.instructions,
            onCancel = viewmodel::onCancel,
            onSubmit = viewmodel::onSubmit,
            onChange = viewmodel::onChangePIN,
        )
    }
}

@Composable
fun PINKeyboardView(
    title: String,
    confirming: Boolean,
    pin: String,
    pin2: String,
    instructions: String,
    onCancel: () -> Unit,
    onChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Title
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 128.dp)
        )


        PINDisplay(
            pin =  if (!confirming) pin else pin2,
            modifier = Modifier.padding(top = 16.dp, start = 64.dp, end = 64.dp),
            onChange = onChange,
            onSubmit = onSubmit
        )

        // Instruction / Error
        Text(
            text = instructions,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 40.dp),
        )

        IconButton(
            onClick = onCancel,
        ) {
            Image(
                imageVector = Icons.Default.Refresh,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                contentDescription = "Try again button",
                modifier = Modifier.size(40.dp)

            )
        }
    }

}

@Composable
fun PINDisplay(
    pin: String,
    onChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                focusRequester.requestFocus()
            },
        contentAlignment = Alignment.Center
    ) {
        // TextField is positioned under the Row and is invisible
        // It's there to capture input
        val customTextSelectionColors = TextSelectionColors(
            handleColor = Color.Transparent,
            backgroundColor = Color.Transparent
        )
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            OutlinedTextField(
                value = pin,
                onValueChange = {
                    if (it.length > 4) return@OutlinedTextField
                    onChange(it)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                keyboardActions = KeyboardActions(onDone = {onSubmit()}),
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .alpha(0f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            (0..3).forEach {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .sizeIn(50.dp, 50.dp)
                        .background(
                            shape = RoundedCornerShape(25f),
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                ) {
                    Text(
                        text = pin.getOrNull(it)?.toString() ?: "0",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Composable
fun EnableBiometricsDialogView(
    title: String,
    description: String,
    onCancel: () -> Unit,
    onAccept: (ActivityResultLauncher<Intent>) -> Unit,
    onResult: (ActivityResult) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 128.dp, start = 48.dp, end = 48.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = description,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = onCancel
            ) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            val enrollLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = onResult
            )

            Button(
                onClick = { onAccept(enrollLauncher) },
            ) {
                Text(
                    text = "Accept",
                    color = MaterialTheme.colorScheme.inversePrimary,
                    style = MaterialTheme.typography.labelMedium
                )
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Setup button"
                )
            }
        }
    }
}
