package com.example.contactapp.data.network

import com.example.contactapp.data.network.datas.Contact
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://randomuser.me/"

val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true // Ignore unknown fields from API response
        })
    }
}

class ContactApiService {
    suspend fun getContacts(page: Int, pageCount: Int): Contact {
        return httpClient.get("${BASE_URL}api") {
            parameter("page", page)
            parameter("results", pageCount) // Corrected from "result" to "results"
        }.body()
    }
}

object ContactNetwork {
    val contactNet = ContactApiService()
}

