package org.bmach01.ackey.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AccessKeyResponse(
    val message: String,
    val key: AccessKey?
)
