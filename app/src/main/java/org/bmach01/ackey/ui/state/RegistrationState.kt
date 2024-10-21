package org.bmach01.ackey.ui.state

import androidx.annotation.StringRes
import org.bmach01.ackey.R

data class RegistrationState(
    val url: String = "",
    val otp: String = "",
    @StringRes val urlError: Int = R.string.emptyString,
    @StringRes val otpError: Int = R.string.emptyString,
    val inputUnlocked: Boolean = true,

    val navigation: Boolean = false
)
