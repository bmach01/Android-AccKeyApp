package org.bmach01.ackey.ui.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.bmach01.ackey.ui.viewmodel.PINViewModel

@Preview
@Composable
fun MainPINKeyboardPreview() {
    MainPINKeyboardView({})
}


@Composable
fun MainPINKeyboardView(
    navigateTo: (route: String) -> Unit
) {
    val context = LocalContext.current
    val viewmodel = viewModel {
        PINViewModel(
            navigateTo = navigateTo,
            context = context
        )
    }
    val uiState = viewmodel.uiState.collectAsState().value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = uiState.title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 128.dp)
        )


        PINDisplay(
            pin =  if (!uiState.confirming) uiState.pin else uiState.pin2,
            modifier = Modifier.padding(top = 16.dp, start = 64.dp, end = 64.dp),
            onChange = viewmodel::onChangePIN,
            onSubmit = viewmodel::onSubmit
        )

        // Instruction / Error
        Text(
            text = uiState.instructions,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 46.dp),
        )

        IconButton(
            onClick = viewmodel::onCancel,
        ) {
            Image(
                imageVector = Icons.Default.Refresh,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                contentDescription = "Try again button",
                modifier = Modifier.size(46.dp)

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
