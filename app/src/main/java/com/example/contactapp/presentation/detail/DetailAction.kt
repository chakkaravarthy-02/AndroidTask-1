package com.example.contactapp.presentation.detail

import com.example.contactapp.domain.Contact

sealed interface DetailAction {
    data class DeleteContact(val contact: Contact?): DetailAction
}