package org.bmach01.ackey.data.repo

import io.ktor.client.call.body
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.bmach01.ackey.data.model.AccessKey
import org.bmach01.ackey.data.model.AccessKeyResponse
import org.bmach01.ackey.data.source.ApiDataSource

class AccessKeyRepo(
    private val dataSource: ApiDataSource = ApiDataSource(),
    private val getToken: suspend () -> String
) {

    suspend fun getAccessKey(): AccessKey {
        // TODO polish this
        return dataSource.fetchKey(getToken()).body<AccessKeyResponse>().key ?: AccessKey("",
            Clock.System.now()
        )
    }
}