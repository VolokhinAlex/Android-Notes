package com.example.java.android1.java_android_notes.repository

import androidx.lifecycle.LiveData
import com.example.java.android1.java_android_notes.model.DataNote
import com.example.java.android1.java_android_notes.room.NoteEntity

interface MainRepository {
    fun getAllNotes(): LiveData<List<NoteEntity>>

    fun getAllFavorites(): LiveData<List<NoteEntity>>

    suspend fun upsertNote(dataNote: DataNote)

    suspend fun removeNote(dataNote: DataNote)

    fun getNotesByQuery(text: String): LiveData<List<NoteEntity>>
}