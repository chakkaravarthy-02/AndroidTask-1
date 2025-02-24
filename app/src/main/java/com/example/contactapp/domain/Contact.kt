package com.example.contactapp.domain

data class Contact(
    val id: Int,
    val resId: String,
    val cell: String,
    val email: String,
    val gender: String,
    val firstName: String,
    val secondName: String,
    val phone: String,
    val picture: String
)
