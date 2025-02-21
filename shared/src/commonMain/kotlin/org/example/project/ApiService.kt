package org.example.project

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object ApiService {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })  // Uses kotlinx.serialization
        }
    }

    /**
     * Generic POST request: Sends [requestBody] as JSON and returns the response deserialized to [R].
     */
    suspend inline fun <reified R, reified T> postData(url: String, requestBody: T): R {
        return client.post(url) {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestBody)
        }.body()
    }
}

