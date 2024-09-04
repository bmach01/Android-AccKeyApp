package org.bmach01.ackey.ui.state

import androidx.compose.ui.graphics.ImageBitmap

data class KeyState(
    val bitmap: ImageBitmap? = null,
    val data: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.".substring(0, 80)
)
