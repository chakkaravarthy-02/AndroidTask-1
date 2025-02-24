package com.example.contactapp.presentation.listing

import com.example.contactapp.domain.Contact

sealed interface ContactAction {
    data class SelectContact(val contact: Contact?): ContactAction
}