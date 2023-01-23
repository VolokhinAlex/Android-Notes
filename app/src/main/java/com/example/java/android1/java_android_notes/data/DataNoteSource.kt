package com.example.java.android1.java_android_notes.data

import java.util.*

interface DataNoteSource {
    interface DataNoteSourceListener {
        fun onItemAdded(index: Int)
        fun onItemRemoved(index: Int)
        fun onItemUpdated(index: Int)
        fun onDataSetChanged()
    }

    val dataNote: List<DataNote>
    fun getItem(index: Int): DataNote?
    val dataNoteCount: Int
    fun createItem(dataNote: DataNote)
    fun removeItem(position: Int)
    fun updateItem(dataNote: DataNote)
    fun addChangesListener(listener: DataNoteSourceListener)
    fun removeChangesListener(listener: DataNoteSourceListener)
    fun filterList(dataNotes: LinkedList<DataNote>)
    fun recreateList()
    fun sortListByDate()
    fun addAndRemoveFavoriteNote(dataNote: DataNote)
    fun sortByFavorite()
}