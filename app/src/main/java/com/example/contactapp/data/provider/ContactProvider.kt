package com.example.contactapp.data.provider

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import com.example.contactapp.data.contactdb.PhoneContactTable
import java.io.ByteArrayOutputStream

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
            val photoIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)
            val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)

            while (it.moveToNext()) {
                val id = it.getString(idIndex)
                val name = it.getString(nameIndex) ?: "Unknown"
                val phone = it.getString(phoneIndex) ?: ""
                val photo = if (photoIndex != -1) it.getString(photoIndex) else null
                val type =
                    if (typeIndex != -1) it.getInt(typeIndex) else ContactsContract.CommonDataKinds.Phone.TYPE_OTHER

                val phoneType = when (type) {
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> "Mobile"
                    ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> "Home"
                    ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> "Work"
                    ContactsContract.CommonDataKinds.Phone.TYPE_OTHER -> "Other"
                    else -> "Unknown"
                }
                contacts.add(
                    PhoneContactTable(
                        id = id,
                        displayName = name,
                        phoneNumber = phone,
                        picture = photo,
                        type = phoneType
                    )
                )
            }
        }

        return contacts
    }

    fun updateContact(nameText: String?, picture: Uri?, phoneText: String?, surnameText: String) {
        val contactId = getContactIdByPhoneNumber(phoneText)
        val operations = arrayListOf<ContentProviderOperation>()

        //update name
        operations.add(
            ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(
                    "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
                    arrayOf(
                        contactId,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                    )
                )
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, nameText)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, surnameText)
                .build()
        )

        //update number
        operations.add(
            ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(
                    "${ContactsContract.Data.RAW_CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
                    arrayOf(contactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                )
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneText)
                .build()
        )

        //update picture
        if (picture != null) {
            val photoBytes = getResizedBitmap(picture)
            if (photoBytes != null) {
                operations.add(
                    ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(
                            "${ContactsContract.Data.RAW_CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
                            arrayOf(contactId, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                        )
                        .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photoBytes)
                        .build()
                )
            }
        }

        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
            println("update success")
        } catch (e: Exception) {
            e.printStackTrace()
            println("update")
        }
    }

    fun saveContact(nameText: String?, picture: Uri?, phoneText: String?, surnameText: String) {
        val operations = arrayListOf<ContentProviderOperation>()

        //add raw contact
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )

        //add name
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, nameText)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, surnameText)
                .build()
        )
        //add number
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneText)
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                )
                .build()
        )

        //add picture
        picture?.let {
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, picture.toString())
                    .build()
            )
        }

        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteContact(phoneNumber: String?) {
        val contactId = getContactIdByPhoneNumber(phoneNumber)
        if (contactId != null) {
            val deleteUri =
                ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId.toLong())
            val rowsDeleted = contentResolver.delete(deleteUri, null, null)
            if (rowsDeleted > 0) {
                println("deleted")
            }
        }
    }

    private fun getContactIdByPhoneNumber(phoneNumber: String?): String? {
        if (phoneNumber.isNullOrEmpty()) return null

        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)

        val selection = "${ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER} = ? OR ${ContactsContract.CommonDataKinds.Phone.NUMBER} = ?"
        val selectionArgs = arrayOf(phoneNumber, phoneNumber)

        contentResolver.query(uri, projection, selection, selectionArgs, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getString(0)
            }
        }
        return null
    }

    private fun getPhotoUri(contactId: String?): String? {
        val uri = ContactsContract.Data.CONTENT_URI
        val projection = arrayOf(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)
        val selection = "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?"
        val selectionArgs = arrayOf(contactId, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)

        contentResolver.query(uri, projection, selection, selectionArgs, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getString(0)
            }
        }
        return null
    }

    private fun getResizedBitmap(imageUri: Uri, maxSize: Int = 512): ByteArray? {
        val inputStream = contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        bitmap?.let {
            val scaledBitmap = Bitmap.createScaledBitmap(it, maxSize, maxSize, true)
            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream) // Compress to JPEG with 75% quality
            return outputStream.toByteArray()
        }
        return null
    }

}