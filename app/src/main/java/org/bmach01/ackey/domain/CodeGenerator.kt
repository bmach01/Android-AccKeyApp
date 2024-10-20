package org.bmach01.ackey.domain

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

class CodeGenerator {
    fun generateQRCode(content: String, width: Int, height: Int, format: BarcodeFormat): Bitmap {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, format, width, height)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.TRANSPARENT)
            }
        }

        return bitmap
    }
}