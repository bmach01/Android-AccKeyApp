package org.bmach01.ackey.data.repo

import android.content.Context
import kotlinx.coroutines.flow.first
import org.bmach01.ackey.data.model.AuthenticationMethod
import org.bmach01.ackey.data.source.LocalDataStore

class SettingsRepo(
    context: Context,
    private val localDataSource: LocalDataStore = LocalDataStore(context)
) {

    suspend fun getAuthenticationMethod(): AuthenticationMethod {
        return AuthenticationMethod.valueOf(
            localDataSource.getStringFlow("authentication_method").first()
        )
    }

    suspend fun saveAuthenticationMethod(method: AuthenticationMethod) {
        localDataSource.saveStringValue("authentication_method", method.name)
    }
}