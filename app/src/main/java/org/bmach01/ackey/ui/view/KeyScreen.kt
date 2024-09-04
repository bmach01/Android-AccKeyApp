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
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.bmach01.ackey.ui.viewmodel.KeyViewModel

@Composable
fun MainKeyView() {
    val viewmodel = viewModel {
        KeyViewModel()
    }
    val uiState = viewmodel.uiState.collectAsState().value
    val noImage = uiState.bitmap == null
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 128.dp)
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(3f),
        ) {
            if (!noImage)
                Image(
                    bitmap = uiState.bitmap!!,
                    contentDescription = "QR Code"
                )

            Text(
                text = uiState.data,
                color = if (noImage) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.primary,
                style = if (noImage) MaterialTheme.typography.headlineLarge
                        else MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
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
                icon = {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh button",
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