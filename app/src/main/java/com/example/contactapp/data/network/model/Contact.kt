package com.example.contactapp.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val results: List<Result>,
    val info: Info
)