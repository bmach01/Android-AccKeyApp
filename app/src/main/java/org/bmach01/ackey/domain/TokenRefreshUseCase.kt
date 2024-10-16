package org.bmach01.ackey.domain

import org.bmach01.ackey.data.model.Credentials
import org.bmach01.ackey.data.repo.AuthenticationRepo
import org.bmach01.ackey.data.repo.SecretRepo

class TokenRefreshUseCase(
    private val secretRepo: SecretRepo,
    private val authenticationRepo: AuthenticationRepo,
) {

    suspend fun refresh() {
        val credentials = Credentials(
            username = secretRepo.getLogin(),
            password = secretRepo.getPassword()
        )

        val token = authenticationRepo.login(credentials)
        secretRepo.saveToken(token)
    }

}