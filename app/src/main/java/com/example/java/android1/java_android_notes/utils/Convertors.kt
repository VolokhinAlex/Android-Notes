package com.example.java.android1.java_android_notes.utils

import com.example.java.android1.java_android_notes.model.DataNote
import com.example.java.android1.java_android_notes.room.NoteEntity

fun convertNoteEntityToDataNote(entity: NoteEntity): DataNote = DataNote(
    id = entity.id,
    noteTitle = entity.noteTitle,
    noteDescription = entity.noteDescription,
    noteFavorite = entity.noteFavorite,
    noteDate = entity.noteDate
)

fun convertDataNoteToNoteEntity(dataNote: DataNote): NoteEntity = NoteEntity(
    id = dataNote.id ?: 0,
    noteTitle = dataNote.noteTitle.orEmpty(),
    noteDescription = dataNote.noteDescription.orEmpty(),
    noteFavorite = dataNote.noteFavorite ?: false,
    noteDate = dataNote.noteDate.orEmpty()
)

fun convertNoteEntityListToDataNoteList(entity: List<NoteEntity>): List<DataNote> = entity.map {
    DataNote(
        id = it.id,
        noteTitle = it.noteTitle,
        noteDescription = it.noteDescription,
        noteFavorite = it.noteFavorite,
        noteDate = it.noteDate
    )
}