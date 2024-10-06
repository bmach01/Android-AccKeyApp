package org.bmach01.ackey.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments
import io.ktor.http.encodedPath
import kotlinx.coroutines.flow.first
import org.bmach01.ackey.data.model.Credentials

class RemoteRepository(
    private val serverUrl: String,
    private val localSettings: LocalSettings,
    private val client: HttpClient = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(
                        username = "test",
                        password = "2137",
                    )
                }
                sendWithoutRequest { request ->
                    request.url.encodedPath.contains("/register") ||
                    request.url.encodedPath.contains("/login")
                }
            }
            bearer {
                loadTokens {
                    BearerTokens(localSettings.token.first(), "")
                }
                refreshTokens {
                    //TODO fetch tokens
                    BearerTokens(localSettings.token.first(), "")
                }
            }
        }
    }
) {
    private suspend fun login(username: String, password: String): HttpStatusCode {
        val response: HttpResponse = client.post(serverUrl) {
            url {
                appendPathSegments("login")
            }
            setBody(Credentials(username, password))
        }

        if (response.status == HttpStatusCode.OK)
            localSettings.saveToken(response.body())

        return response.status
    }

    private suspend fun register(otp: String): HttpStatusCode {
        val response: HttpResponse = client.post(serverUrl) {
            url {
                appendPathSegments("register")
            }
            setBody(otp)
        }

        if (response.status == HttpStatusCode.OK) {
            val credentials: Credentials = response.body()
            localSettings.saveUsername(credentials.username)
            localSettings.savePassword(credentials.password)
        }

        return response.status
    }

    private suspend fun fetchKey(): HttpStatusCode {
        val response: HttpResponse = client.post(serverUrl) {
            url {
                appendPathSegments("key").appendPathSegments("accessKey")
            }
        }

        if (response.status == HttpStatusCode.OK)
            localSettings.saveKey(response.body())

        return response.status

    }

    private suspend fun deactivateAccount(): HttpStatusCode {
        val response: HttpResponse = client.post(serverUrl) {
            url {
                appendPathSegments("user").appendPathSegments("deactivate")
            }
            setBody(Credentials(localSettings.username.first(), localSettings.password.first()))
        }

        return response.status
    }

}