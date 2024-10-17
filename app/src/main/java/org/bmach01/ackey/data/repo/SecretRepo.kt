package org.bmach01.ackey.data.repo

import kotlinx.coroutines.flow.first
import org.bmach01.ackey.data.CryptoManager
import org.bmach01.ackey.data.source.LocalDataStore
import javax.inject.Inject

class SecretRepo @Inject constructor(
    private val localDataStore: LocalDataStore,
) {
    private val cryptoManager: CryptoManager = CryptoManager

    companion object {
        private const val PASSWORD_KEY = "password"
        private const val LOGIN_KEY = "login"
        private const val TOKEN_KEY = "token"
        private const val PIN_KEY = "pin"
    }

    suspend fun getPassword(): String {
        val encrypted = localDataStore.getStringFlow(PASSWORD_KEY).first()
        if (encrypted.isNullOrEmpty()) return ""
        val decrypted = cryptoManager.decrypt(encrypted.toByteArray())

        return decrypted.map { it.toInt().toChar() }.joinToString("")
    }

    suspend fun savePassword(password: String) {
        val encrypted = cryptoManager.encrypt(password.toByteArray())
        localDataStore.saveStringValue(PASSWORD_KEY, String(encrypted))
    }

    suspend fun getToken(): String {
        val encrypted = localDataStore.getStringFlow(TOKEN_KEY).first()
        if (encrypted.isNullOrEmpty()) return ""
        val decrypted = cryptoManager.decrypt(encrypted.toByteArray())

        return decrypted.map { it.toInt().toChar() }.joinToString("")
    }

    suspend fun saveToken(token: String) {
        val encrypted = cryptoManager.encrypt(token.toByteArray())
        localDataStore.saveStringValue(TOKEN_KEY, String(encrypted))
    }

    suspend fun getLogin(): String {
        val encrypted = localDataStore.getStringFlow(LOGIN_KEY).first()
        if (encrypted.isNullOrEmpty()) return ""
        val decrypted = cryptoManager.decrypt(encrypted.toByteArray())

        return decrypted.map { it.toInt().toChar() }.joinToString("")
    }

    suspend fun saveLogin(login: String) {
        val encrypted = cryptoManager.encrypt(login.toByteArray())
        localDataStore.saveStringValue(LOGIN_KEY, String(encrypted))
    }

    suspend fun getPIN(): String {
        val encrypted = localDataStore.getStringFlow(PIN_KEY).first()
        if (encrypted.isNullOrEmpty()) return ""
        val decrypted = cryptoManager.decrypt(encrypted.toByteArray())

        return decrypted.map { it.toInt().toChar() }.joinToString("")
    }

    suspend fun savePIN(pin: String) {
        val encrypted = cryptoManager.encrypt(pin.toByteArray())

        localDataStore.saveStringValue(PIN_KEY, String(encrypted))
    }
}