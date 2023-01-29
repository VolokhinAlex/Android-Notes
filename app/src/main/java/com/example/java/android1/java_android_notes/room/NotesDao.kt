package com.example.java.android1.java_android_notes.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {
    @Upsert
    fun upsert(note: NoteEntity)

    @Query("SELECT * FROM notes_table")
    fun getAllNotes(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes_table WHERE note_favorite = 1")
    fun getAllFavorites(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes_table WHERE LOWER(note_title) LIKE '%' || :text || '%' " +
            "OR LOWER(note_description) LIKE '%' || :text || '%'")
    fun getNotesByQuery(text: String): LiveData<List<NoteEntity>>

    @Delete
    fun delete(note: NoteEntity)
}