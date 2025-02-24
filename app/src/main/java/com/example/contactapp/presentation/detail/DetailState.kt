package com.example.contactapp.presentation.detail

import com.example.contactapp.domain.Contact

data class DetailState (
    val contact: Contact? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)