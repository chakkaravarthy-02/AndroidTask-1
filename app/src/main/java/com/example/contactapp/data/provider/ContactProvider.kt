package com.example.contactapp.data.provider

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import com.example.contactapp.data.contactdb.PhoneContactTable

class ContactProvider(
    private val contentResolver: ContentResolver
) {
    fun getPhoneContacts(): List<PhoneContactTable> {
        val contacts = mutableListOf<PhoneContactTable>()
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val emailIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
            val photoIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)

            while (it.moveToNext()){
                val id = it.getString(idIndex)
                val name = it.getString(nameIndex) ?: "Unknown"
                val phone = it.getString(phoneIndex) ?: ""
                val email = if(emailIndex != -1) it.getString(emailIndex) else null
                val photo = if(photoIndex != -1) it.getString(photoIndex) else null

                contacts.add(PhoneContactTable(id = id, displayName = name, phoneNumber = phone, email = email, gender = null, picture = photo))
            }
        }

        return contacts
    }
}