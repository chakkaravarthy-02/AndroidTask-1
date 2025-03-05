package com.example.contactapp.presentation.listing

data class ContactViewState(
    val isSelectedContact: Boolean = false,
    var selectedContactId: Int? = 0
)
