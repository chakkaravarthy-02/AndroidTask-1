package com.example.contactapp.data.network.datas

import kotlinx.serialization.Serializable

@Serializable
data class Info(
    val page: Int,
    val results: Int,
    val seed: String,
    val version: String
)