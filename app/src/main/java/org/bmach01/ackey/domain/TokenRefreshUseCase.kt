package org.bmach01.ackey.domain

import android.util.Log
import org.bmach01.ackey.data.model.Credentials
import org.bmach01.ackey.data.repo.AuthenticationRepo
import org.bmach01.ackey.data.repo.SecretRepo

class TokenRefreshUseCase(
    private val secretRepo: SecretRepo,
    private val authenticationRepo: AuthenticationRepo,
) {

    suspend fun refresh() {
        Log.d("bmach", "refresh token!")

        val credentials = Credentials(
            username = secretRepo.getLogin(),
            password = secretRepo.getPassword()
        )

        val token = authenticationRepo.login(credentials)
        secretRepo.saveToken(token)

        Log.d("bmach", "new token: ${token}")
//        return token
    }

}