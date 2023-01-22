package com.example.java.android1.java_android_notes.data

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import java.util.*

class DataNoteSourceFirebase : BaseDataNoteSource() {
    private val mFirebase = FirebaseFirestore.getInstance()
    private val mCollections = mFirebase.collection(COLLECTIONS_NOTES)

    init {
        mCollections.orderBy(DataNoteFromFirebase.FIELD_DATE, Query.Direction.DESCENDING)
            .get().addOnCompleteListener { task: Task<QuerySnapshot> -> onFetchComplete(task) }
            .addOnFailureListener { exception: Exception -> onFetchFailure(exception) }
    }

    private fun onFetchComplete(task: Task<QuerySnapshot>) {
        val data = LinkedList<DataNote>()
        for (queryDocumentSnapshot in task.result) {
            data.add(
                DataNoteFromFirebase(
                    queryDocumentSnapshot.id,
                    queryDocumentSnapshot.data
                )
            )
        }
        mDataNotes.clear()
        mDataNotes.addAll(data)
        data.clear()
        sortByFavorite()
        notifyDataSetChanged()
    }

    private fun onFetchFailure(exception: Exception) {
        Log.e(TAG_DEBUG, "Fetch exception: ", exception)
    }

    override fun createItem(dataNote: DataNote) {
        val dataFromFirebase: DataNoteFromFirebase = initCard(dataNote)
        mDataNotes.add(dataFromFirebase)
        mCollections.add(dataFromFirebase.fields)
            .addOnSuccessListener { documentReference: DocumentReference ->
                dataFromFirebase.id = documentReference.id
            }
        notifyAdded(mDataNotes.size - 1)
    }

    override fun removeItem(position: Int) {
        val id = mDataNotes[position].id
        mCollections.document(id!!).delete()
        super.removeItem(position)
    }

    override fun updateItem(dataNote: DataNote) {
        val dataFromFirebase: DataNoteFromFirebase
        val id = dataNote.id
        if (id != null) {
            var index = 0
            for (note in mDataNotes) {
                if (id == note.id) {
                    note.noteName = dataNote.noteName
                    note.noteDescription = dataNote.noteDescription
                    note.noteDate = dataNote.noteDate
                    note.noteFavorite = dataNote.noteFavorite
                    dataFromFirebase = initCard(dataNote)
                    mCollections.document(id).update(dataFromFirebase.fields)
                        .addOnFailureListener { exception: Exception? ->
                            Log.w(
                                TAG_DEBUG, "Error updating document", exception
                            )
                        }
                    notifyUpdated(index)
                    return
                }
                index++
            }
        }
    }

    private fun initCard(dataNote: DataNote): DataNoteFromFirebase {
        val dataFromFirebase: DataNoteFromFirebase = if (dataNote is DataNoteFromFirebase) {
            dataNote
        } else {
            DataNoteFromFirebase(dataNote)
        }
        return dataFromFirebase
    }

    override fun recreateList() {
        mCollections.orderBy(DataNoteFromFirebase.FIELD_DATE, Query.Direction.DESCENDING)
            .get().addOnCompleteListener { task: Task<QuerySnapshot> -> onFetchComplete(task) }
            .addOnFailureListener { exception: Exception -> onFetchFailure(exception) }
    }

    override fun addAndRemoveFavoriteNote(dataNote: DataNote) {
        val dataFromFirebase: DataNoteFromFirebase
        val id = dataNote.id
        if (id != null) {
            var index = 0
            for (note in mDataNotes) {
                if (id == note.id) {
                    note.noteFavorite = dataNote.noteFavorite
                    dataFromFirebase = initCard(dataNote)
                    mCollections.document(id).update(dataFromFirebase.fields)
                        .addOnFailureListener { exception: Exception? ->
                            Log.w(
                                TAG_DEBUG, "Error updating document", exception
                            )
                        }
                    return
                }
                index++
            }
        }
    }

    companion object {
        private const val COLLECTIONS_NOTES = "android.notes.CollectionsNotes"
        private const val TAG_DEBUG = "Firebase.Exception"
        private val LOCK_KEY = Any()

        @Volatile
        private var sInstance: DataNoteSourceFirebase? = null
        @JvmStatic
        val instance: DataNoteSourceFirebase?
            get() {
                var instance = sInstance
                if (instance == null) {
                    synchronized(LOCK_KEY) {
                        if (sInstance == null) {
                            instance = DataNoteSourceFirebase()
                            sInstance = instance
                        }
                    }
                }
                return instance
            }
    }
}