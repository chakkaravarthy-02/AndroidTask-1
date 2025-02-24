package com.example.contactapp.data.network.datas

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val cell: String,
    val email: String,
    val gender: String,
    val id: Id,
    val name: Name,
    val phone: String,
    val picture: Picture
)