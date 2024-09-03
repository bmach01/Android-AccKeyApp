package org.bmach01.ackey.ui.views

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

@Composable
fun MainKeyView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 128.dp)
    ) {
        // Show qr code / bar code
        QRCodeDisplay(content = "TESTESTESTEST", width = 1024, height = 512)

        Spacer(modifier = Modifier.weight(1f))
        // two buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val buttonModifier = Modifier.weight(1f).padding(horizontal = 8.dp).fillMaxHeight();

            KeyViewButton(action = { /*TODO*/ }, text = "test 1", modifier = buttonModifier)
            KeyViewButton(action = { /*TODO*/ }, text = "test 2", modifier = buttonModifier)
        }
    }
}

@Composable
fun KeyViewButton(
    action: () -> Unit,
    text: String,
    modifier: Modifier
) {
    OutlinedButton(
        onClick = action,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun QRCodeDisplay(content: String, width: Int, height: Int) {
    val qrBitmap = generateQRCode(content, width, height)
    Image(bitmap = qrBitmap.asImageBitmap(), contentDescription = "QR Code")
}

fun generateQRCode(content: String, width: Int, height: Int): Bitmap {
    val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, width, height)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }

    return bitmap
}