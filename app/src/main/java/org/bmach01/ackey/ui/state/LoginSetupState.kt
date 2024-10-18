package org.bmach01.ackey.ui.state

import org.bmach01.ackey.domain.BiometricHelper

data class LoginSetupState(
    val pin: String = "",
    val pin2: String = "",
    val confirming: Boolean = false,
    val title: String = "Setup PIN",
    val instructions: String = "Lorem ipsum here will be the instruction Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum",

    val biometricTitle: String = "System authentication",
    val biometricInstruction: String = "Do you want to use authentication system provided by your device (pattern, fingerprint etc.)?",
    val isBiometricSetupOpen: Boolean = false,
    val biometricResult: BiometricHelper.BiometricResult? = null,

    val navigation: Boolean = false
)