package org.bmach01.ackey.data.source

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.options
import io.ktor.client.request.url

internal object HttpClientProvider {
    val client: HttpClient = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    suspend fun setServerBaseUrl(url: String) {
        client.options {
            url(url)
        }
    }
}