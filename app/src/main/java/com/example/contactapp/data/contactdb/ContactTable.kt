package com.example.contactapp.data.contactdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
data class ContactTable(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val resId: String,
    @ColumnInfo val cell: String,
    @ColumnInfo val email: String,
    @ColumnInfo val gender: String,
    @ColumnInfo val firstName: String,
    @ColumnInfo val secondName: String,
    @ColumnInfo val phone: String,
    @ColumnInfo val picture: String,
    @ColumnInfo val uuid: String
)

@Entity(indices = [Index(value = ["id"], unique = true)])
data class PhoneContactTable(
    @PrimaryKey val id: String,
    @ColumnInfo val displayName: String,
    @ColumnInfo val phoneNumber: String?,
    @ColumnInfo val picture: String?,
    @ColumnInfo val type: String?
)