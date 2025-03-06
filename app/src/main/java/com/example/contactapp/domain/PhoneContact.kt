package com.example.contactapp.domain

data class PhoneContact(
    override val id: String,
    override val displayName: String,
    val email: String?,
    val gender: String?,
    val phoneNumber: String?,
    override val picture: String?,
    override val firstName: String? = null,
    override val secondName: String? = null
) : BaseContact