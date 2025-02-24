package com.example.contactapp.data.network.datas

import kotlinx.serialization.Serializable

@Serializable
data class Id(
    val name: String,
    val value: String?
)