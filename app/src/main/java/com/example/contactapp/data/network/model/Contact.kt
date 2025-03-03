package com.example.contactapp.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    @SerialName("results")
    val results: List<Result>,
    @SerialName("info")
    val info: Info
)