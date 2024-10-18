package org.bmach01.ackey.ui.state


data class RegistrationState(
    val url: String = "",
    val otp: String = "",
    val urlError: String = "",
    val otpError: String = "",
    val inputUnlocked: Boolean = true,

    val navigation: Boolean = false
)
