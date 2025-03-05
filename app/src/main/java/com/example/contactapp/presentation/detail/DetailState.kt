package com.example.contactapp.presentation.detail

import com.example.contactapp.domain.Contact

data class DetailViewState(
    val loading: Boolean = false,
    var selectedContact: Contact? = null,
    val error: String? = null,
)