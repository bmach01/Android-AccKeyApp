package org.bmach01.ackey.ui.view

import org.bmach01.ackey.R
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.bmach01.ackey.ui.viewmodel.LoginSetupViewModel

@Preview
@Composable
fun MainLoginSetupPreview() {
    MainLoginSetupView( {} )
}

@Composable
fun MainLoginSetupView(
    navigateToKey: () -> Unit,
    viewmodel: LoginSetupViewModel = hiltViewModel()
) {
    BackHandler(true) {}
    // TODO: split it to two viewmodels for reusability(?)
    val uiPINState by viewmodel.uiPINState.collectAsState()
    val uiBiometricState by viewmodel.uiBiometricState.collectAsState()

    if (uiPINState.navigation)
        LaunchedEffect(Unit) {
            navigateToKey()
        }

    if (uiBiometricState.isBiometricSetupOpen and viewmodel.isBiometricHelperInitialized) {
        EnableBiometricsDialogView(
            title = stringResource(uiBiometricState.biometricTitle),
            description = stringResource(uiBiometricState.biometricInstruction),
            onCancel = viewmodel::onBiometricSetupCancel,
            onAccept = viewmodel::onBiometricSetupAccept,
            onResult = viewmodel::onBiometricSetupResult,
        )
    }
    else {
        PINKeyboardView(
            title = stringResource(uiPINState.title),
            confirming = uiPINState.confirming,
            pin = uiPINState.pin,
            pin2 = uiPINState.pin2,
            instructions = stringResource(uiPINState.instructions),
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
            modifier = Modifier.size(40.dp)
        ) {
            Image(
                imageVector = Icons.Default.Refresh,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                contentDescription = "Try again button",
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
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            // For catching activity result
            val enrollLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = onResult
            )

            Button(
                onClick = { onAccept(enrollLauncher) },
            ) {
                Text(
                    text = stringResource(R.string.accept),
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
