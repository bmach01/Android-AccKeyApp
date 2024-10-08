package org.bmach01.ackey.data.repo

import io.ktor.client.call.body
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.http.HttpStatusCode
import org.bmach01.ackey.data.source.ApiDataSource

class AuthenticationRepo(
    private val dataSource: ApiDataSource = ApiDataSource(),
) {

    suspend fun login(credentials: BasicAuthCredentials): String? {
        val response = dataSource.login(credentials)

        return if (response.status != HttpStatusCode.OK)
            response.body()
        else
            null
    }

    suspend fun register(otp: String): BasicAuthCredentials? {
        val response = dataSource.register(otp)

        return if (response.status != HttpStatusCode.OK)
            response.body()
        else
            null
    }
}