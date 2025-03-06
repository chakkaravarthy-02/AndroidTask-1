package com.example.contactapp.domain

data class Contact(
    override val id: Int,
    val resId: String,
    val cell: String,
    val email: String,
    val gender: String,
    override val firstName: String?,
    override val secondName: String?,
    val phone: String,
    override val picture: String?,
    val uuid: String,
    override val displayName: String?
): BaseContact
