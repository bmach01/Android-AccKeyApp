package org.bmach01.ackey.data.source

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

class LocalDataStore(private val context: Context) {

    suspend fun saveStringValue(key: String, value: String) {
        context.dataStore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    fun getStringFlow(key: String): Flow<String> {
        return context.dataStore.data.map { it[stringPreferencesKey(key)] ?: "" }.distinctUntilChanged()
    }

}