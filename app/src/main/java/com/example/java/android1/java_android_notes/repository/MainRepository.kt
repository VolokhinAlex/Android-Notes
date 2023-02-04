package com.example.java.android1.java_android_notes.repository

import androidx.lifecycle.LiveData
import com.example.java.android1.java_android_notes.model.DataNote
import com.example.java.android1.java_android_notes.room.NoteEntity

interface MainRepository {
    fun getAllNotes(): LiveData<List<NoteEntity>>

    fun getAllFavorites(): LiveData<List<NoteEntity>>

    suspend fun insertNote(dataNote: DataNote)

    suspend fun updateNote(dataNote: DataNote)

    suspend fun deleteNote(dataNote: DataNote)

    fun getNotesByQuery(text: String): LiveData<List<NoteEntity>>
}