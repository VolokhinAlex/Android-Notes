package com.example.java.android1.java_android_notes.app

import com.example.java.android1.java_android_notes.model.DataNote

sealed class RoomAppState {
    data class Success(val data: List<DataNote>) : RoomAppState()
    data class Error(val error: Throwable) : RoomAppState()
    object Loading : RoomAppState()
}
