package org.bmach01.ackey.data.repo

import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import org.bmach01.ackey.data.model.AccessKey
import org.bmach01.ackey.data.source.ApiDataSource

class AccessKeyRepo(
    private val dataSource: ApiDataSource = ApiDataSource(),
    private val getToken: () -> String
) {

    suspend fun getAccessKey(): AccessKey? {
        val response = dataSource.fetchKey(getToken())

        return if (response.status != HttpStatusCode.OK)
            response.body()
        else
            null
    }
}