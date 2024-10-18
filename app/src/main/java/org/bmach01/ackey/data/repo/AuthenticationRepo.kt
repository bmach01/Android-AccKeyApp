package org.bmach01.ackey.data.repo

import io.ktor.client.call.body
import org.bmach01.ackey.data.model.Credentials
import org.bmach01.ackey.data.source.ApiDataSource
import javax.inject.Inject

class AuthenticationRepo @Inject constructor(
    private val dataSource: ApiDataSource
) {

    suspend fun login(credentials: Credentials): String {
        // TODO polish this maybe
        return dataSource.login(credentials).body<Map<String, String>>()["token"] ?: ""
    }

    suspend fun register(otp: String): Credentials {
        return dataSource.register(otp).body()
    }
}