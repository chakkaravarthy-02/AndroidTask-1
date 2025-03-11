package com.example.contactapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhoneContact(
    override val id: String,
    override val displayName: String,
    val phoneNumber: String?,
    override val picture: String?,
    override val firstName: String? = null,
    override val secondName: String? = null,
    val type: String?
) : BaseContact,Parcelable