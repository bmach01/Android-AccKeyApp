package org.bmach01.ackey.ui.state

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.datetime.Instant

data class KeyState(
    val bitmap: ImageBitmap? = null,
    val key: String? = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.".substring(0, 80),
    val validUntil: Instant? = null,
    val error: String? = null
)
