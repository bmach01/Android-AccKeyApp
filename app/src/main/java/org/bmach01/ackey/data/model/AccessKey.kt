package org.bmach01.ackey.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class AccessKey(
    val key: String,
    val validUntil: Instant,
)
