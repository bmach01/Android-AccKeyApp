package org.bmach01.ackey.data.repo

import android.content.Context
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.serialization.StringFormat
import org.bmach01.ackey.data.CryptoManager
import org.bmach01.ackey.data.source.LocalDataStore
import java.nio.charset.Charset

class SecretRepo(context: Context) {
    private val cryptoManager: CryptoManager = CryptoManager()
    private val localDataStore: LocalDataStore = LocalDataStore(context)

    private val PASSWORD_KEY = "password"
    private val LOGIN_KEY = "login"
    private val TOKEN_KEY = "token"
    private val PIN_KEY = "pin"

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