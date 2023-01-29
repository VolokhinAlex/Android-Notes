package com.example.java.android1.java_android_notes.repository

import androidx.annotation.WorkerThread
import com.example.java.android1.java_android_notes.model.DataNote
import com.example.java.android1.java_android_notes.room.NotesDao
import com.example.java.android1.java_android_notes.utils.convertDataNoteToNoteEntity

class DetailsRepositoryImpl(private val dao: NotesDao) : DetailsRepository {

    @WorkerThread
    override suspend fun upsertNote(dataNote: DataNote) {
        dao.upsert(convertDataNoteToNoteEntity(dataNote))
    }

}