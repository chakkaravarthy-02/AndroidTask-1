package com.example.contactapp.data.contactdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ContactTable::class, PhoneContactTable::class], version = 6, exportSchema = false)
abstract class ContactDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}

private lateinit var INSTANCE: ContactDatabase

fun getDatabase(context: Context): ContactDatabase {
    synchronized(ContactDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context,
                ContactDatabase::class.java,
                "ContactTable"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}