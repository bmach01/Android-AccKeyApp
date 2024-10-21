package org.bmach01.ackey.ui.state

import org.bmach01.ackey.R
import androidx.annotation.StringRes
import org.bmach01.ackey.domain.BiometricHelper

data class BiometricSetupState(
    @StringRes val biometricTitle: Int = R.string.system_authentication,
    @StringRes val biometricInstruction: Int = R.string.system_authentication_default_instruction,
    val isBiometricSetupOpen: Boolean = false,
    val biometricResult: BiometricHelper.BiometricResult? = null,
)
