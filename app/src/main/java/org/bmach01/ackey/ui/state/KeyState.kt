package org.bmach01.ackey.ui.state

import androidx.compose.ui.graphics.ImageBitmap
import org.bmach01.ackey.data.model.AccessKey

data class KeyState(
    val key: AccessKey? = null,
    val bitmap: ImageBitmap? = null,
    val isLoadingKey: Boolean = true,
    val error: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
)
