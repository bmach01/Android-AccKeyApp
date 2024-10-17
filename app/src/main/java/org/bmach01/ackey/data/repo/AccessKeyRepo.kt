package org.bmach01.ackey.data.repo

import io.ktor.client.call.body
import org.bmach01.ackey.data.model.AccessKey
import org.bmach01.ackey.data.source.ApiDataSource
import javax.inject.Inject

class AccessKeyRepo @Inject constructor(
    private val dataSource: ApiDataSource
) {

    suspend fun getAccessKey(token: String): AccessKey {
        return dataSource.fetchKey(token).body<AccessKey>()
    }
}