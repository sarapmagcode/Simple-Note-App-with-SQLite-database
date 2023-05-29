package com.example.xmlnoteapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.xmlnoteapp.data.Note
import com.example.xmlnoteapp.data.NoteDao
import kotlinx.coroutines.launch

class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes().asLiveData()

    /** Private class methods **/
    private fun insertNote(note: Note) {
        viewModelScope.launch { noteDao.insert(note) }
    }

    private fun createNewNote(noteTitle: String, noteContent: String, noteTimestamp: String): Note {
        return Note(
            noteTitle = noteTitle,
            noteContent = noteContent,
            noteTimestamp = noteTimestamp
        )
    }

    private fun updateNote(note: Note) {
        viewModelScope.launch { noteDao.update(note) }
    }

    private fun getUpdatedNote(
        id: Int,
        noteTitle: String,
        noteContent: String,
        noteTimestamp: String
    ): Note {
        return Note(
            id = id,
            noteTitle = noteTitle,
            noteContent = noteContent,
            noteTimestamp = noteTimestamp
        )
    }

    /** Public class methods **/
    fun addNewNote(noteTitle: String, noteContent: String, noteTimestamp: String) {
        val newNote = createNewNote(noteTitle, noteContent, noteTimestamp)
        insertNote(newNote)
    }

    fun isEntryValid(noteTitle: String, noteContent: String): Boolean {
        if (noteTitle.isBlank() || noteContent.isBlank()) {
            return false
        }
        return true
    }

    fun getSpecificNote(id: Int): LiveData<Note> {
        return noteDao.getSpecificNote(id).asLiveData()
    }

    fun updateNote(id: Int, noteTitle: String, noteContent: String, noteTimestamp: String) {
        val updatedNote = getUpdatedNote(id, noteTitle, noteContent, noteTimestamp)
        updateNote(updatedNote)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch { noteDao.delete(note) }
    }
}

class NoteViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}