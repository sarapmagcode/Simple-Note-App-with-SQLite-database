package com.example.xmlnoteapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val noteTitle: String,

    @ColumnInfo(name = "content")
    val noteContent: String,

    @ColumnInfo(name = "timestamp")
    val noteTimestamp: String
)