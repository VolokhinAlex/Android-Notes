package com.example.java.android1.java_android_notes.data

interface DataAuthSource {
    val dataAuth: List<DataAuth>
    fun createItem(dataAuth: DataAuth)
    fun clear()
}