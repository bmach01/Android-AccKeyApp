package org.bmach01.ackey.ui.state

import org.bmach01.ackey.R
import androidx.annotation.StringRes

data class LoginSetupState(
    val pin: String = "",
    val pin2: String = "",
    val confirming: Boolean = false,
    @StringRes val title: Int = R.string.setup_pin,
    @StringRes val instructions: Int = R.string.setup_pin_default_instruction,

    val navigation: Boolean = false
)