package com.example.java.android1.java_android_notes.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.java.android1.java_android_notes.model.DataNote
import com.example.java.android1.java_android_notes.room.NoteEntity
import com.example.java.android1.java_android_notes.room.NotesDao
import com.example.java.android1.java_android_notes.utils.convertDataNoteToNoteEntity

class MainRepositoryImpl(private val dao: NotesDao) : MainRepository {

    override fun getAllNotes(): LiveData<List<NoteEntity>> =
        dao.getAllNotes()

    override fun getAllFavorites(): LiveData<List<NoteEntity>> =
        dao.getAllFavorites()

    @WorkerThread
    override suspend fun insertNote(dataNote: DataNote) {
        dao.insert(convertDataNoteToNoteEntity(dataNote))
    }

    @WorkerThread
    override suspend fun updateNote(dataNote: DataNote) {
        dao.update(convertDataNoteToNoteEntity(dataNote))
    }

    @WorkerThread
    override suspend fun deleteNote(dataNote: DataNote) {
        dao.delete(note = convertDataNoteToNoteEntity(dataNote = dataNote))
    }

    override fun getNotesByQuery(text: String): LiveData<List<NoteEntity>> =
        dao.getNotesByQuery(text = text)

}