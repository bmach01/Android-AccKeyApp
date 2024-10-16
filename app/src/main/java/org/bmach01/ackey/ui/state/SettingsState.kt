package org.bmach01.ackey.ui.state

import org.bmach01.ackey.data.model.AuthenticationMethod
import org.bmach01.ackey.ui.AppScreen

data class SettingsState(
    val systemAuthentication: Boolean = false,
    val pinAuthentication: Boolean = true,

    val systemAuthenticationEnabled: Boolean = true,
    val pinAuthenticationEnabled: Boolean = true,

    val authenticationMethod: AuthenticationMethod = AuthenticationMethod.PIN,

    val navigation: AppScreen = AppScreen.SettingsScreen
)
