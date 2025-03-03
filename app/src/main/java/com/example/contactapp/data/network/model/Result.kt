package com.example.contactapp.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName("cell")
    val cell: String,
    @SerialName("email")
    val email: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("id")
    val id: Id,
    @SerialName("name")
    val name: Name,
    @SerialName("phone")
    val phone: String,
    @SerialName("picture")
    val picture: Picture,
    @SerialName("login")
    val login: Login,
    @SerialName("dob")
    val dob: Dob,
//    @SerialName("location")
//    val location: Location,
    @SerialName("registered")
    val registered: Registered
)