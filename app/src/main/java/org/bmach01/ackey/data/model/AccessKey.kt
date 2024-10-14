package org.bmach01.ackey.data.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class AccessKey(
    val key: String,
    val validUntil: Instant,
)
