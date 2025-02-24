package com.example.contactapp.data.network

import com.example.contactapp.data.contactdb.ContactTable
import com.example.contactapp.domain.Contact
import com.example.contactapp.data.network.datas.Result


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
    )
}
