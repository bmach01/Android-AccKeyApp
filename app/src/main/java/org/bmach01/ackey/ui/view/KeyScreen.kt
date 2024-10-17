package org.bmach01.ackey.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import org.bmach01.ackey.ui.AppScreen
import org.bmach01.ackey.ui.viewmodel.KeyViewModel

@Preview
@Composable
fun KeyScreenPreview() {
    MainKeyView({})
}

@Composable
fun MainKeyView(
    navigateTo: (route: String) -> Unit,
    viewmodel: KeyViewModel = hiltViewModel()
) {
    val uiState = viewmodel.uiState.collectAsState().value

    if (uiState.navigation != AppScreen.KeyScreen)
        LaunchedEffect(Unit) {
            navigateTo(uiState.navigation.name)
            // Here should change uiState.navigation = AppScreen.KeyScreen
        }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 128.dp)
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(3f),
        ) {
            KeyOrErrorDisplay(
                isLoading = uiState.isLoadingKey,
                bitmap = uiState.bitmap,
                keyText = uiState.key?.key ?: "",
                error = uiState.error,
                validUntil = uiState.key?.validUntil
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val buttonModifier = Modifier.padding(horizontal = 8.dp).size(100.dp)
            val iconModifier = Modifier.size(40.dp)

            KeyViewButton(
                onClick = viewmodel::onRefresh,
                modifier = buttonModifier,
                enabled = !uiState.isLoadingKey,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh button",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = iconModifier
                    )
                }
            )
            KeyViewButton(
                onClick = viewmodel::navigateToSettings,
                modifier = buttonModifier,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings button",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = iconModifier
                    )
                }
            )
        }
    }
}

@Composable
fun KeyViewButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier,
    enabled: Boolean = true
) {
    FilledTonalIconButton (
        onClick = onClick,
        shape = RoundedCornerShape(50f),
        modifier = modifier,
        enabled = enabled
    ) {
        icon()
    }
}

@Composable
fun KeyOrErrorDisplay(
    isLoading: Boolean,
    bitmap: ImageBitmap?,
    keyText: String,
    error: String,
    validUntil: Instant?
) {
    if (isLoading) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 8.dp,
            modifier = Modifier.size(128.dp)
        )
    }
    else {
        if (bitmap != null) {

            Surface(
                color = Color.White
            ) {
                Image(
                    bitmap = bitmap,
                    contentDescription = "QR Code",
                    modifier = Modifier.padding(8.dp)
                )
            }

            Text(
                text = keyText,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )

            if (validUntil != null) {
                val customFormat = DateTimeComponents.Format {
                    hour(); char(':'); minute(); char(':'); second();
                }
                Text(
                    text = "Valid until: ${validUntil.format(customFormat)}",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        else {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}