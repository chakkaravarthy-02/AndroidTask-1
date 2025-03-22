package com.example.contactapp.domain

import android.net.Uri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.contactapp.data.contactdb.ContactDatabase
import com.example.contactapp.data.contactdb.ContactTable
import com.example.contactapp.data.network.ContactApiService
import com.example.contactapp.data.network.toPhoneContact
import com.example.contactapp.data.provider.ContactProvider

class ContactRepository(
    private val apiService: ContactApiService,
    private val database: ContactDatabase,
    private val contactProvider: ContactProvider
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getContacts(): Pager<Int, ContactTable> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                enablePlaceholders = false,
                prefetchDistance = 2
            ),
            remoteMediator = ContactRemoteMediator(database, apiService),
            pagingSourceFactory = { database.contactDao().pagingSource() }
        )
    }

    suspend fun setAllPhoneContactsIntoDatabase() {
        val allPhoneContacts = contactProvider.getPhoneContacts().sortedBy { it.displayName }
        database.contactDao().upsertPhoneContacts(allPhoneContacts)
    }

    fun getAllPhoneContacts(): List<PhoneContact> {
        return database.contactDao().getAllPhoneContacts().toPhoneContact()
    }

    fun updateContact(
        id: String?,
        nameText: String?,
        phoneText: String?,
        surnameText: String,
        picture: Uri?
    ): String? {
        return contactProvider.updateContact(id,nameText,picture,phoneText,surnameText)
    }

    fun saveContact(
        nameText: String?,
        phoneText: String?,
        surnameText: String,
        picture: Uri?
    ): String? {
        return contactProvider.saveContact(nameText,picture,phoneText,surnameText)
    }

    suspend fun deleteContact(phoneNumber: String?) {
        contactProvider.deleteContact(phoneNumber)
        if (phoneNumber != null) {
            database.contactDao().deleteContactByNumber(phoneNumber)
        }
    }
}