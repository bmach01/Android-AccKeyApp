package org.bmach01.ackey.ui.state

import org.bmach01.ackey.R
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.ImageBitmap
import org.bmach01.ackey.data.model.AccessKey

data class KeyState(
    val key: AccessKey? = null,
    val bitmap: ImageBitmap? = null,
    val isLoadingKey: Boolean = true,
    @StringRes val error: Int = R.string.emptyString,
)
