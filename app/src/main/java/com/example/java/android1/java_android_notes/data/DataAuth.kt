package com.example.java.android1.java_android_notes.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataAuth(
    val email: String?,
    val fullName: String?,
    val imageProfile: Uri?
) : Parcelable
