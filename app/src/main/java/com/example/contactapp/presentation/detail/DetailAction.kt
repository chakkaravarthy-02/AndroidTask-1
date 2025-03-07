package com.example.contactapp.presentation.detail

import com.example.contactapp.domain.PhoneContact

sealed interface DetailAction {
    data class DeleteContact(val contact: PhoneContact?): DetailAction
}