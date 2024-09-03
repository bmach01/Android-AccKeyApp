package org.bmach01.ackey.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "local_settings")

class LocalSettings(private val context: Context) {

    private val dataStore: DataStore<Preferences> get() = context.dataStore

    private companion object {
        val AUTHENTICATION_METHOD = stringPreferencesKey("authentication_method")
    }

    suspend fun saveAuthenticationMethod(method: AuthenticationMethod) {
        context.dataStore.edit {
            it[AUTHENTICATION_METHOD] = method.name
        }
    }

    val authenticationMethod: Flow<AuthenticationMethod> = context.dataStore.data.map {
        // Password is the default value
        AuthenticationMethod.valueOf(
            it[AUTHENTICATION_METHOD] ?: AuthenticationMethod.PASSWORD.name
        )
    }

}