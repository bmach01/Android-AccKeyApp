package org.bmach01.ackey.ui.state

import org.bmach01.ackey.data.model.AuthenticationMethod

data class LoginState(
    val pin: String = "",
    val authenticationMethod: AuthenticationMethod = AuthenticationMethod.PIN,
    val error: String = "",
    val authenticated: Boolean = false
)
