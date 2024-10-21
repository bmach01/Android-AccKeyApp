package org.bmach01.ackey.ui.state

import org.bmach01.ackey.R
import androidx.annotation.StringRes
import org.bmach01.ackey.data.model.AuthenticationMethod

data class LoginState(
    val pin: String = "",
    val authenticationMethod: AuthenticationMethod = AuthenticationMethod.PIN,
    @StringRes val error: Int = R.string.emptyString,
    val authenticated: Boolean = false
)
