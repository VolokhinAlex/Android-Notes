package com.example.java.android1.java_android_notes.data

import java.util.*

class DataNoteFromFirebase(
    mNoteName: String?,
    mNoteDescription: String?,
    mNoteFavorite: Boolean,
    mNoteDate: String?
) : DataNote(
    noteName = mNoteName,
    noteDescription = mNoteDescription,
    noteFavorite = mNoteFavorite,
    noteDate = mNoteDate
) {

    constructor(
        id: String?,
        title: String?,
        description: String?,
        favorite: Boolean,
        date: String?
    ) : this(title, description, favorite, date) {
        this.id = id
    }

    constructor(id: String?, fields: Map<String?, Any?>) : this(
        id, fields[FIELD_TITLE] as String?,
        fields[FIELD_DESCRIPTION] as String?,
        (fields[FIELD_FAVORITE] as Boolean?)!!,
        fields[FIELD_DATE] as String?
    )

    constructor(dataNote: DataNote) : this(
        dataNote.id, dataNote.noteName,
        dataNote.noteDescription, dataNote.noteFavorite, dataNote.noteDate
    )

    val fields: Map<String, Any?>
        get() {
            val hashMap = HashMap<String, Any?>()
            hashMap[FIELD_TITLE] = noteName
            hashMap[FIELD_DESCRIPTION] = noteDescription
            hashMap[FIELD_FAVORITE] = noteFavorite
            hashMap[FIELD_DATE] = noteDate
            return Collections.unmodifiableMap(hashMap)
        }

    companion object {
        const val FIELD_ID = "id"
        const val FIELD_TITLE = "title"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_FAVORITE = "favorite"
        const val FIELD_DATE = "date"
    }
}