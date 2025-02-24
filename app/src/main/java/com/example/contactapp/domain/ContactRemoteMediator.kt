package com.example.contactapp.domain

import android.util.Log
import retrofit2.HttpException
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.contactapp.data.contactdb.ContactDatabase
import com.example.contactapp.data.contactdb.ContactTable
import com.example.contactapp.data.network.ContactApiService
import com.example.contactapp.data.network.asDatabaseModel
import okio.IOException

@OptIn(ExperimentalPagingApi::class)
class ContactRemoteMediator(
    private val db: ContactDatabase,
    private val contactApi: ContactApiService
) : RemoteMediator<Int, ContactTable>(){
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ContactTable>
    ): MediatorResult {
        return try{
            val loadKey = when(loadType){
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            val contacts = contactApi.getContacts(
                page = loadKey,
                pageCount = state.config.pageSize
            )

            db.withTransaction{
                if(loadType == LoadType.REFRESH){
                    db.contactDao().clearAll()
                }
                val contactTable = contacts.results.asDatabaseModel()
                db.contactDao().upsertAll(contactTable)
            }

            MediatorResult.Success(
                endOfPaginationReached = contacts.results.isEmpty()
            )
        } catch (e: IOException){
            MediatorResult.Error(e)
        } catch (e: HttpException){
            MediatorResult.Error(e)
        }
    }
}