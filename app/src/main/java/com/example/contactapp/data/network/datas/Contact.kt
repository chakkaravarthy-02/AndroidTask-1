package com.example.contactapp.data.network.datas

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val results: List<Result>,
    val info: Info
)