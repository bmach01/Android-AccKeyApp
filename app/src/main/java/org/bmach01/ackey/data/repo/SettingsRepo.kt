package org.bmach01.ackey.data.repo

import android.content.Context
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.first
import org.bmach01.ackey.data.model.AuthenticationMethod
import org.bmach01.ackey.data.source.ApiDataSource
import org.bmach01.ackey.data.source.LocalDataStore

class SettingsRepo(
    context: Context,
    private val localDataSource: LocalDataStore = LocalDataStore(context),
    private val remoteDataSource: ApiDataSource = ApiDataSource(),
    private val getToken: () -> String
) {

    suspend fun getAuthenticationMethod(): AuthenticationMethod {
        return AuthenticationMethod.valueOf(
            localDataSource.getStringFlow("authentication_method").first()
        )
    }

    suspend fun saveAuthenticationMethod(method: AuthenticationMethod) {
        localDataSource.saveStringValue("authentication_method", method.name)
    }

    suspend fun deactivateAccount(credentials: BasicAuthCredentials): Boolean {
        val response = remoteDataSource.deactivateAccount(credentials, getToken())

        return response.status == HttpStatusCode.OK
    }
}