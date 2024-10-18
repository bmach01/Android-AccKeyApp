package org.bmach01.ackey.data.repo

import kotlinx.coroutines.flow.first
import org.bmach01.ackey.data.model.AuthenticationMethod
import org.bmach01.ackey.data.source.HttpClientProvider
import org.bmach01.ackey.data.source.LocalDataStore
import javax.inject.Inject

class SettingsRepo @Inject constructor(
    private val localDataSource: LocalDataStore
) {

    suspend fun getAuthenticationMethod(): AuthenticationMethod {
        return AuthenticationMethod.valueOf(
            localDataSource.getStringFlow("authentication_method").first() ?: AuthenticationMethod.PIN.name
        )
    }

    suspend fun saveAuthenticationMethod(method: AuthenticationMethod) {
        localDataSource.saveStringValue("authentication_method", method.name)
    }

    suspend fun saveServerBaseUrl(url: String) {
        HttpClientProvider.serverUrl = url
        localDataSource.saveStringValue("base_url", url)
    }

    suspend fun getServerBaseUrl(): String {
        val url = localDataSource.getStringFlow("base_url").first() ?: ""
        HttpClientProvider.serverUrl = url
        return url
    }

    suspend fun clearAllLocalData() {
        localDataSource.clearAll()
    }
}