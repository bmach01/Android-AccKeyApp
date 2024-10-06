package org.bmach01.ackey.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "local_settings")

class LocalSettings(private val context: Context) {

    private companion object {
        val AUTHENTICATION_METHOD = stringPreferencesKey("authentication_method")
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
        val KEY = stringPreferencesKey("key")
        val TOKEN = stringPreferencesKey("jwt")
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
    }.distinctUntilChanged()

    suspend fun saveToken(token: String) {
        context.dataStore.edit {
            it[TOKEN] = token
        }
    }
    val token: Flow<String> = context.dataStore.data.map {
        it[TOKEN] ?: ""
    }.distinctUntilChanged()

    suspend fun saveUsername(username: String) {
        context.dataStore.edit {
            it[USERNAME] = username
        }
    }
    val username: Flow<String> = context.dataStore.data.map {
        it[USERNAME] ?: ""
    }.distinctUntilChanged()

    suspend fun savePassword(password: String) {
        context.dataStore.edit {
            it[PASSWORD] = password
        }
    }
    val password: Flow<String> = context.dataStore.data.map {
        it[PASSWORD] ?: ""
    }.distinctUntilChanged()

    suspend fun saveKey(key: String) {
        context.dataStore.edit {
            it[KEY] = key
        }
    }
    val key: Flow<String> = context.dataStore.data.map {
        it[KEY] ?: ""
    }.distinctUntilChanged()
}