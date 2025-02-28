package com.example.contactapp.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Id(
    val name: String,
    val value: String?
)