package com.example.contactapp.data.contactdb

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Upsert
import androidx.room.Query

@Dao
interface ContactDao {

    @Upsert
    suspend fun upsertAll(contactTable: List<ContactTable>)

    @Query("SELECT * FROM ContactTable")
    fun pagingSource(): PagingSource<Int, ContactTable>

    @Query("DELETE FROM ContactTable")
    suspend fun clearAll()
}