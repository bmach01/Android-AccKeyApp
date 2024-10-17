package org.bmach01.ackey.data

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object CryptoManager {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
    private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"

    private val encryptCipher get() = Cipher.getInstance(TRANSFORMATION).apply {
        init(Cipher.ENCRYPT_MODE, getKey())
    }

    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry("acKeySecret", null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    "acKeySecret",
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    private fun getSizeBytes(size: Int): ByteArray {
        return byteArrayOf(
                (size shr 24).toByte(),
                (size shr 16).toByte(),
                (size shr 8).toByte(),
                size.toByte()
        )
    }

    private fun getSizeFromBytes(bytes: ByteArray): Int {
        return (bytes[0].toInt() shl 24) or
                ((bytes[1].toInt() and 0xFF) shl 16) or
                ((bytes[2].toInt() and 0xFF) shl 8) or
                (bytes[3].toInt() and 0xFF)
    }

    fun encrypt(bytes: ByteArray): ByteArray {
        val cipher = encryptCipher
        val encrypted = cipher.doFinal(bytes)
        val sizeBytes = getSizeBytes(encrypted.size)
        val iv = cipher.iv

        return Base64.encode(
            byteArrayOf(iv.size.toByte()) +
                    iv +
                    sizeBytes +
                    encrypted,
            Base64.DEFAULT)
    }

    fun decrypt(bytes64: ByteArray): ByteArray {
        // ByteArray should look like IV_SIZE | IV | MESSAGE_SIZE_BYTES | MESSAGE
        val bytes = Base64.decode(bytes64, Base64.DEFAULT)

        // Pointer for moving, and extracting data from the ByteArray
        var pointer = 0
        val ivSize = bytes[pointer++].toInt()

        val iv = bytes.sliceArray(pointer until ivSize + pointer)

        pointer += ivSize

        val messageSize = getSizeFromBytes(bytes.sliceArray(pointer until pointer + 4))
        pointer += 4 // size of the message is coded over 4 bytes (int)

        val encryptedMessage = bytes.sliceArray(pointer until pointer + messageSize)

        return getDecryptCipherForIv(iv).doFinal(encryptedMessage)
    }
}