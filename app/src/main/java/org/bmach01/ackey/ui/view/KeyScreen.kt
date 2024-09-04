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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.bmach01.ackey.domain.CodeGenerator

@Composable
fun MainKeyView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 128.dp)
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(3f),
        ) {
            val code = "TESTESTESTEST"
            QRCodeDisplay(content = code, width = 1024, height = 512)
            Text(
                text = code,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelLarge,
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
                onClick = { /*TODO*/ },
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
                onClick = { /*TODO*/ },
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

@Composable
fun QRCodeDisplay(content: String, width: Int, height: Int) {
    val generator = CodeGenerator()
    val qrBitmap = generator.generateQRCode(content, width, height)
    Image(bitmap = qrBitmap.asImageBitmap(), contentDescription = "QR Code")
}