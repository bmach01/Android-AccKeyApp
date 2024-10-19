package org.bmach01.ackey.ui.state

import org.bmach01.ackey.domain.BiometricHelper

data class BiometricSetupState(
    val biometricTitle: String = "System authentication",
    val biometricInstruction: String = "Do you want to use authentication system provided by your device (pattern, fingerprint etc.)?",
    val isBiometricSetupOpen: Boolean = false,
    val biometricResult: BiometricHelper.BiometricResult? = null,
)
