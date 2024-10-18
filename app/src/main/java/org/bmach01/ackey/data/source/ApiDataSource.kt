package org.bmach01.ackey.data.source

import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import org.bmach01.ackey.data.model.Credentials
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiDataSource @Inject constructor() {
    private val client: HttpClient = HttpClientProvider.client

    suspend fun login(credentials: Credentials): HttpResponse {
        return client.post {
            url {
                appendPathSegments("login")
            }
            contentType(ContentType.Application.Json)
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

    suspend fun deactivateAccount(credentials: Credentials, token: String): HttpResponse {
        return client.post {
            url {
                appendPathSegments("user").appendPathSegments("deactivate")
            }
            contentType(ContentType.Application.Json)
            setBody(credentials)
            headers {
                bearerAuth(token)
            }
        }
    }

}