package com.example.contactapp.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Info(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: Int,
    @SerialName("seed")
    val seed: String,
    @SerialName("version")
    val version: String
)