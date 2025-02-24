package com.example.contactapp.presentation.listing

import com.example.contactapp.domain.Contact

data class ContactState(
    val contacts: List<Contact> = emptyList(),
    val selectedContact: Contact? = null,
    val isSelectedContact: Boolean = false
)
