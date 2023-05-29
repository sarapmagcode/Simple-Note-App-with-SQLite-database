package com.example.xmlnoteapp

import android.app.Application
import com.example.xmlnoteapp.data.NoteRoomDatabase

class NoteApplication : Application() {
    val database: NoteRoomDatabase by lazy { NoteRoomDatabase.getDatabase(this) }
}