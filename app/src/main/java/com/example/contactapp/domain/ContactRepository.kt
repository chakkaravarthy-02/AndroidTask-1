package com.example.contactapp.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.contactapp.data.contactdb.ContactDatabase
import com.example.contactapp.data.contactdb.ContactTable
import com.example.contactapp.data.network.ContactApiService

class ContactRepository(
    private val apiService: ContactApiService,
    private val database: ContactDatabase
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getContacts(): Pager<Int,ContactTable> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                enablePlaceholders = false,
                prefetchDistance = 2
            ),
            remoteMediator = ContactRemoteMediator(database,apiService),
            pagingSourceFactory = { database.contactDao().pagingSource() }
        )
    }
}