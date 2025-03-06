package com.example.contactapp.presentation.detail

import com.example.contactapp.domain.Contact
import com.example.contactapp.domain.PhoneContact

data class DetailViewState(
    val loading: Boolean = false,
    var selectedContact: Contact? = null,
    var selectedPhoneContact: PhoneContact? = null,
    val error: String? = null,
    var isPhoneContact: Boolean? = null
)