package com.example.java.android1.java_android_notes.data

import android.annotation.SuppressLint
import com.example.java.android1.java_android_notes.data.DataNoteSource.DataNoteSourceListener
import java.lang.Boolean
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Int
import kotlin.let

abstract class BaseDataNoteSource : DataNoteSource {
    private val mListeners = HashSet<DataNoteSourceListener>()

    @JvmField
    protected val mDataNotes = LinkedList<DataNote>()

    override fun addChangesListener(listener: DataNoteSourceListener) {
        mListeners.add(listener)
    }

    override fun removeChangesListener(listener: DataNoteSourceListener) {
        mListeners.remove(listener)
    }

    override val dataNote: List<DataNote>
        get() = Collections.unmodifiableList(mDataNotes)


    override fun getItem(index: Int): DataNote {
        return mDataNotes[index]
    }

    override val dataNoteCount: Int
        get() = mDataNotes.size

    override fun createItem(dataNote: DataNote) {
        mDataNotes.add(dataNote)
        val index = mDataNotes.size - 1
        for (listener in mListeners) {
            listener.onItemAdded(index)
        }
    }

    override fun removeItem(position: Int) {
        mDataNotes.removeAt(position)
        for (listener in mListeners) {
            listener.onItemRemoved(position)
        }
    }

    protected fun notifyAdded(index: Int) {
        for (listener in mListeners) {
            listener.onItemAdded(index)
        }
    }

    protected fun notifyUpdated(index: Int) {
        for (listener in mListeners) {
            listener.onItemUpdated(index)
        }
    }

    protected fun notifyDataSetChanged() {
        for (listener in mListeners) {
            listener.onDataSetChanged()
        }
    }

    override fun filterList(dataNotes: LinkedList<DataNote>) {
        mDataNotes.clear()
        mDataNotes.addAll(dataNotes)
        notifyDataSetChanged()
    }

    @SuppressLint("SimpleDateFormat")
    override fun sortListByDate() {
        val dataNotes = LinkedList(mDataNotes)
        dataNotes.sortWith sort@{ dataNote: DataNote, t1: DataNote ->
            val format: DateFormat = SimpleDateFormat("dd.MM.yyyy")
            try {
                return@sort Objects.requireNonNull(dataNote.noteDate?.let { format.parse(it) })
                    .compareTo(t1.noteDate?.let { format.parse(it) })
            } catch (e: ParseException) {
                return@sort 0
            }
        }
        mDataNotes.clear()
        mDataNotes.addAll(dataNotes)
        dataNotes.clear()
        notifyDataSetChanged()
    }

    override fun sortByFavorite() {
        val dataNotes = LinkedList(mDataNotes)
        dataNotes.sortWith { dataNote: DataNote, dataNote2: DataNote ->
            Boolean.compare(
                !dataNote.noteFavorite,
                !dataNote2.noteFavorite
            )
        }
        mDataNotes.clear()
        mDataNotes.addAll(dataNotes)
        dataNotes.clear()
        notifyDataSetChanged()
    }
}