package org.bmach01.ackey.data.model

import java.util.Date

data class AccessKey(
    val key: String,
    val validUntil: Date,
)
