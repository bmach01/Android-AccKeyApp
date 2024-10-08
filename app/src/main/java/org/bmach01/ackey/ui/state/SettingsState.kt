package org.bmach01.ackey.ui.state

import org.bmach01.ackey.data.model.AuthenticationMethod

data class SettingsState(
    val faceAuthentication: Boolean = false,
    val fingerAuthentication: Boolean = false,
    val passwordAuthentication: Boolean = true,

    val faceAuthenticationEnabled: Boolean = true,
    val fingerAuthenticationEnabled: Boolean = true,
    val passwordAuthenticationEnabled: Boolean = true,

    val authenticationMethod: AuthenticationMethod = AuthenticationMethod.PASSWORD
)
