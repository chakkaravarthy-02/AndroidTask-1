package com.example.contactapp.data.contactdb

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ContactDao {

    @Upsert
    suspend fun upsertAll(contactTable: List<ContactTable>)

    @Upsert
    suspend fun upsertPhoneContacts(phoneContactTable: List<PhoneContactTable>)

    @Query("SELECT * FROM ContactTable")
    fun pagingSource(): PagingSource<Int, ContactTable>

    @Query("SELECT * FROM PhoneContactTable")
    fun getAllPhoneContacts(): List<PhoneContactTable>

    @Query("DELETE FROM ContactTable")
    suspend fun clearAll()

    @Query("SELECT * FROM ContactTable")
    fun getAll(): List<ContactTable>

    @Query("DELETE FROM PhoneContactTable WHERE phoneNumber = :number")
    suspend fun deleteContactByNumber(number: String)
}