package org.bmach01.ackey.data.source

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.appendPathSegments

class ApiDataSource(
    private val client: HttpClient = HttpClientProvider.client
) {
    suspend fun login(credentials: BasicAuthCredentials): HttpResponse {
        return client.post {
            url {
                appendPathSegments("login")
            }
            setBody(credentials)
        }
    }

    suspend fun register(otp: String): HttpResponse {
        return client.post {
            url {
                appendPathSegments("register")
            }
            setBody(otp)
        }
    }

    suspend fun fetchKey(token: String): HttpResponse {
        return client.post {
            url {
                appendPathSegments("key").appendPathSegments("accessKey")
            }
            headers {
                bearerAuth(token)
            }
        }


    }

    suspend fun deactivateAccount(credentials: BasicAuthCredentials, token: String): HttpResponse {
        return client.post {
            url {
                appendPathSegments("user").appendPathSegments("deactivate")
            }
            setBody(credentials)
            headers {
                bearerAuth(token)
            }
        }
    }

}