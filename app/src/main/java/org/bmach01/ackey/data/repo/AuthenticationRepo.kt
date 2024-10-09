package org.bmach01.ackey.data.repo

import io.ktor.client.call.body
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.http.HttpStatusCode
import org.bmach01.ackey.data.model.Credentials
import org.bmach01.ackey.data.source.ApiDataSource

class AuthenticationRepo(
    private val dataSource: ApiDataSource = ApiDataSource(),
    private val getToken: suspend () -> String
) {

    suspend fun login(credentials: Credentials): String {
        return dataSource.login(credentials).body()
    }

    suspend fun register(otp: String): Credentials {
        return dataSource.register(otp).body()
    }

    suspend fun deactivateAccount(credentials: BasicAuthCredentials): Boolean {
        val response = dataSource.deactivateAccount(credentials, getToken())

        return response.status == HttpStatusCode.OK
    }
}