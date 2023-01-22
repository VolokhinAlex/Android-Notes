package com.example.java.android1.java_android_notes.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class DataNote(
    var id: String? = null,
    var noteName: String?,
    var noteDescription: String?,
    var noteDate: String?,
    var noteFavorite: Boolean = false
) : Parcelable

