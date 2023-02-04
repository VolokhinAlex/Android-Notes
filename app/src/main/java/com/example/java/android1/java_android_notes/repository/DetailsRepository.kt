package com.example.java.android1.java_android_notes.repository

import com.example.java.android1.java_android_notes.model.DataNote

interface DetailsRepository {
    suspend fun updateNote(dataNote: DataNote)
}
