package org.bmach01.ackey.data.repo

import io.ktor.http.HttpStatusCode
import org.bmach01.ackey.data.model.Credentials
import org.bmach01.ackey.data.source.ApiDataSource
import javax.inject.Inject

class UserManagementRepo @Inject constructor(
    private val dataSource: ApiDataSource
) {

    suspend fun deactivateAccount(credentials: Credentials, token: String): Boolean {
        val response = dataSource.deactivateAccount(credentials, token)

        return response.status == HttpStatusCode.OK
    }
}