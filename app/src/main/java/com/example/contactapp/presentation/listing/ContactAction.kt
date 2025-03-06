package com.example.contactapp.presentation.listing

import com.example.contactapp.domain.Contact
import com.example.contactapp.domain.PhoneContact

sealed interface ContactAction {
    data class SelectContactInApi(val contact: Contact?): ContactAction
    data class SelectContactInPhone(val contact: PhoneContact?): ContactAction
}