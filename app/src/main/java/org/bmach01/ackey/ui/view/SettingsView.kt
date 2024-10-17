package org.bmach01.ackey.ui.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.bmach01.ackey.data.model.AuthenticationMethod
import org.bmach01.ackey.ui.AppScreen
import org.bmach01.ackey.ui.viewmodel.SettingsViewModel

@Preview
@Composable
fun MainSettingsPreview() {
    MainSettingsView({ return@MainSettingsView true })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSettingsView(
    goBack: () -> Boolean,
    viewmodel: SettingsViewModel = hiltViewModel()
) {
    val uiState = viewmodel.uiState.collectAsState().value

    if (uiState.goBack)
        LaunchedEffect(Unit) {
            Log.d("bmach", "went back ${goBack()}")
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = viewmodel::goBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back_button"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(it)
        ) {
            val categoryModifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)

            val rowModifier = Modifier
                .padding(start = 32.dp, bottom = 8.dp, end = 32.dp)
                .fillMaxWidth()
                .height(40.dp)

            SettingsCategory(
                label = "Authentication method",
                modifier = categoryModifier,
                body = {
                    SettingsRadioRow(
                        label = "System authentication",
                        state = uiState.systemAuthentication,
                        onChange = { viewmodel.onSwitch(method = AuthenticationMethod.SYSTEM) },
                        modifier = rowModifier
                    )

                    SettingsRadioRow(
                        label = "PIN authentication",
                        state = uiState.pinAuthentication,
                        onChange = { viewmodel.onSwitch(method = AuthenticationMethod.PIN) },
                        modifier = rowModifier
                    )
                }
            )

            SettingsCategory(
                label = "Account management",
                modifier = categoryModifier,
                body = {
                    SettingsButton(
                        label = "Change PIN",
                        onClick = { /* ... */ },
                        modifier = rowModifier,
                        icon = Icons.Default.Edit
                    )

                    SettingsButton(
                        label = "Unregister device",
                        onClick = { /* ... */ },
                        modifier = rowModifier,
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
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun SettingsRadioRow(
    label: String,
    state: Boolean,
    onChange: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .clip(RoundedCornerShape(50f))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable(
                enabled = enabled,
                role = Role.RadioButton,
                onClick = {
                    onChange()
                }
            )
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 12.dp)
        )

        RadioButton(
            selected = state,
            onClick = {
                onChange()
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
    FilledTonalIconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "$label icon",
                    modifier = Modifier.padding(start = 12.dp, end = 8.dp)
                )
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