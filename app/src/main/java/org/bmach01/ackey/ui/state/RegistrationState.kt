package org.bmach01.ackey.ui.state

import org.bmach01.ackey.ui.AppScreen

data class RegistrationState(
    val url: String = "",
    val otp: String = "",
    val urlError: String = "",
    val otpError: String = "",
    val inputUnlocked: Boolean = true,

    val navigation: AppScreen = AppScreen.RegisterScreen
)
