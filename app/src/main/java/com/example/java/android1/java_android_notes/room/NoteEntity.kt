package com.example.java.android1.java_android_notes.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "note_title")
    val noteTitle:String,
    @ColumnInfo(name = "note_description")
    val noteDescription: String,
    @ColumnInfo(name = "note_favorite")
    val noteFavorite: Boolean,
    @ColumnInfo(name = "note_date")
    val noteDate: String
)