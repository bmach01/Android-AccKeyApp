package org.bmach01.ackey.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun MainRegisterView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 128.dp)
    ) {
        val inputModifier = Modifier
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .fillMaxWidth()

        // URL input
        RegisterInput(
            onChange = { /*TODO*/ },
            label = "Domain URL",
            placeholder = "www.domain.com",
            modifier = inputModifier
        )


        // OTP input
        RegisterInput(
            onChange = { /*TODO*/ },
            label = "One Time Password",
            placeholder = "Password...",
            visible = false,
            modifier = inputModifier
        )

        Spacer(modifier = Modifier.height(128.dp))

        FilledTonalIconButton(
            onClick = { /*TODO*/ },
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
    label: String,
    placeholder: String,
    modifier: Modifier,
    visible: Boolean = true
) {
    OutlinedTextField(
        value = "",
        onValueChange = onChange,
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        },
        label = {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier
    )
}