package org.bmach01.ackey.data.repo

import android.content.Context
import kotlinx.coroutines.flow.first
import org.bmach01.ackey.data.CryptoManager
import org.bmach01.ackey.data.source.LocalDataStore

class SecretRepo(context: Context) {
    private val cryptoManager: CryptoManager = CryptoManager()
    private val localDataStore: LocalDataStore = LocalDataStore(context)

    private val PASSWORD_KEY = "password"
    private val LOGIN_KEY = "login"
    private val TOKEN_KEY = "token"
    private val PIN_KEY = "pin"

    suspend fun getPassword(): String {
        val encrypted = localDataStore.getStringFlow(PASSWORD_KEY).first()
        return cryptoManager.decrypt(encrypted.toByteArray()).toString()
    }

    suspend fun getToken(): String {
        val encrypted = localDataStore.getStringFlow(TOKEN_KEY).first()
        return cryptoManager.decrypt(encrypted.toByteArray()).toString()
    }

    suspend fun getLogin(): String {
        val encrypted = localDataStore.getStringFlow(LOGIN_KEY).first()
        return cryptoManager.decrypt(encrypted.toByteArray()).toString()
    }

    suspend fun savePassword(password: String) {
        val encrypted = cryptoManager.encrypt(password.toByteArray()).toString()
        localDataStore.saveStringValue(PASSWORD_KEY, encrypted)
    }

    suspend fun saveToken(token: String) {
        val encrypted = cryptoManager.encrypt(token.toByteArray()).toString()
        localDataStore.saveStringValue(TOKEN_KEY, encrypted)
    }

    suspend fun saveLogin(login: String) {
        val encrypted = cryptoManager.encrypt(login.toByteArray()).toString()
        localDataStore.saveStringValue(LOGIN_KEY, encrypted)
    }
}