package com.example.java.android1.java_android_notes.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataNote(
    val id: Long?,
    val noteTitle: String?,
    val noteDescription: String?,
    val noteFavorite: Boolean?,
    val noteDate: String?
) : Parcelable