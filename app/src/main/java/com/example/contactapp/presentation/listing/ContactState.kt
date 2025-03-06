package com.example.contactapp.presentation.listing

import com.example.contactapp.domain.PhoneContact

data class ContactViewState(
    var selectedContactId: Int? = 0,
    var selectedPhoneContactId: String? = null,
    var phoneContactsList: List<PhoneContact> = emptyList()
)
