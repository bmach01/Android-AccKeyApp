package org.bmach01.ackey.ui.views

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSettingsView() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
            })
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(it)
        ) {
            val categoryModifier = Modifier
                .fillMaxWidth()
                .heightIn(200.dp)
                .padding(16.dp)

            val switchRowModifier = Modifier
                .padding(start = 32.dp, bottom = 8.dp, end = 32.dp)
                .fillMaxWidth()

            val buttonRowModifier = Modifier
                .padding(start = 24.dp, bottom = 8.dp, end = 32.dp)
                .fillMaxWidth()

            // Biometric authentication
            SettingsCategory(
                label = "Biometric authentication",
                modifier = categoryModifier,
                body = {
                    SettingsSwitch(
                        label = "Face authentication",
                        state = true,
                        onChange = { /* ... */ },
                        modifier = switchRowModifier
                    )

                    SettingsSwitch(
                        label = "Fingerprint authentication",
                        state = true,
                        onChange = { /* ... */ },
                        modifier = switchRowModifier
                    )

                    SettingsSwitch(
                        label = "Password authentication",
                        state = true,
                        onChange = { /* ... */ },
                        modifier = switchRowModifier
                    )
                }
            )

            // Account settings
            SettingsCategory(
                label = "Account management",
                modifier = categoryModifier,
                body = {
                    SettingsButton(
                        label = "Change password",
                        onClick = { /* ... */ },
                        modifier = buttonRowModifier,
                        icon = Icons.Default.Edit
                    )

                    SettingsButton(
                        label = "Delete account",
                        onClick = { /* ... */ },
                        modifier = buttonRowModifier,
                        icon = Icons.Default.Delete
                    )
                }
            )
        }
    }
}

@Composable
fun SettingsCategory(
    label: String,
    body: @Composable () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
        )

        body()
    }
}

@Composable
fun SettingsSwitch(
    label: String,
    state: Boolean,
    onChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Switch,
                onClick = {
                    onChange(!state)
                }
            )
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelLarge
        )
        Switch(
            checked = state,
            onCheckedChange = onChange,
            thumbContent = if (state) {
                {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else {
                null
            }

        )
    }
}

@Composable
fun SettingsButton(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier,
    icon: ImageVector
) {
    TextButton (
        onClick = onClick,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "$label icon",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "$label arrow",
            )
        }
    }
}