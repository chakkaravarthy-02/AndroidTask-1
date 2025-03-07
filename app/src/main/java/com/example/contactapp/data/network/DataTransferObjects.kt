package com.example.contactapp.data.network

import com.example.contactapp.data.contactdb.ContactTable
import com.example.contactapp.data.contactdb.PhoneContactTable
import com.example.contactapp.domain.Contact
import com.example.contactapp.data.network.model.Result
import com.example.contactapp.domain.PhoneContact


fun List<Result>.asDatabaseModel(): List<ContactTable> {
    return map {
        ContactTable(
            resId = it.id.name,
            cell = it.cell,
            email = it.email,
            gender = it.gender,
            firstName = it.name.first,
            secondName = it.name.last,
            phone = it.phone,
            picture = it.picture.medium,
            uuid = it.login.uuid
        )
    }
}

fun ContactTable.toContact(): Contact {
    return Contact(
        id = id,
        resId = resId,
        cell = cell,
        email = email,
        gender = gender,
        firstName = firstName,
        secondName = secondName,
        phone = phone,
        picture = picture,
        uuid = uuid,
        displayName = null
    )
}

fun List<PhoneContactTable>.toPhoneContact() : List<PhoneContact>{
    return map{
        PhoneContact(
            id = it.id,
            displayName = it.displayName,
            phoneNumber = it.phoneNumber,
            picture = it.picture
        )
    }
}
