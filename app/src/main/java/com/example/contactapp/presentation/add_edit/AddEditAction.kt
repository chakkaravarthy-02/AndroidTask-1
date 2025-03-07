package com.example.contactapp.presentation.add_edit

import android.net.Uri

sealed interface AddEditAction {
    data class SaveContact(
        val nameText: String?,
        val phoneText: String?,
        val surnameText: String,
        val picture: Uri?
    ) : AddEditAction
    data class EditContact(
        val nameText: String?,
        val phoneText: String?,
        val surnameText: String,
        val picture: Uri?
    ) : AddEditAction
}