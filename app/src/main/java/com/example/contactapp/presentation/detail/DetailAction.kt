package com.example.contactapp.presentation.detail

sealed interface DetailAction {
    data class Load(val contactId: String?): DetailAction
}