package com.example.java.android1.java_android_notes.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

public class DataNoteSourceFirebase extends BaseDataNoteSource {

    private static final String COLLECTIONS_NOTES = "android.notes.CollectionsNotes";
    private static final String TAG_DEBUG = "Firebase.Exception";
    private final FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();
    private static final Object LOCK_KEY = new Object();

    private volatile static DataNoteSourceFirebase sInstance;
    private CollectionReference mCollections = mFirebase.collection(COLLECTIONS_NOTES);

    public static DataNoteSourceFirebase getInstance() {
        DataNoteSourceFirebase instance = sInstance;
        if (instance == null) {
            synchronized (LOCK_KEY) {
                if (sInstance == null) {
                    instance = new DataNoteSourceFirebase();
                    sInstance = instance;
                }
            }
        }
        return instance;
    }

    private DataNoteSourceFirebase() {
        mCollections.orderBy(DataNoteFromFirebase.FIELD_DATE, Query.Direction.DESCENDING).get().
                addOnCompleteListener(this::onFetchComplete).
                addOnFailureListener(this::onFetchFailure);
    }

    private void onFetchComplete(Task<QuerySnapshot> task) {
        LinkedList<DataNote> data = new LinkedList<>();
        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
            data.add(new DataNoteFromFirebase(queryDocumentSnapshot.getId(),
                    queryDocumentSnapshot.getData()));
        }
        mDataNotes.clear();
        mDataNotes.addAll(data);
        data.clear();
        sortByFavorite();
        notifyDataSetChanged();
    }

    private void onFetchFailure(@NonNull Exception exception) {
        Log.e(TAG_DEBUG, "Fetch exception: ", exception);
    }

    @Override
    public void createItem(@NonNull DataNote dataNote) {
        final DataNoteFromFirebase dataFromFirebase;
        dataFromFirebase = initCard(dataNote);
        mDataNotes.add(dataFromFirebase);
        mCollections.add(dataFromFirebase.getFields()).
                addOnSuccessListener(documentReference ->
                        dataFromFirebase.setId(documentReference.getId()));
    }

    @Override
    public void removeItem(int position) {
        String id = mDataNotes.get(position).getId();
        mCollections.document(id).delete();
        super.removeItem(position);
    }

    @Override
    public void updateItem(@NonNull DataNote dataNote) {
        final DataNoteFromFirebase dataFromFirebase;
        String id = dataNote.getId();
        if (id != null) {
            int index = 0;
            for (DataNote note : mDataNotes) {
                if (id.equals(note.getId())) {
                    note.setNoteName(dataNote.getNoteName());
                    note.setNoteDescription(dataNote.getNoteDescription());
                    note.setNoteDate(dataNote.getNoteDate());
                    note.setNoteFavorite(dataNote.getNoteFavorite());
                    dataFromFirebase = initCard(dataNote);
                    mCollections.document(id).update(dataFromFirebase.getFields()).
                            addOnFailureListener((exception) -> Log.w(TAG_DEBUG, "Error updating document", exception));
                    notifyUpdated(index);
                    return;
                }
                index++;
            }
        }
    }

    private DataNoteFromFirebase initCard(DataNote dataNote) {
        final DataNoteFromFirebase dataFromFirebase;
        if (dataNote instanceof DataNoteFromFirebase) {
            dataFromFirebase = (DataNoteFromFirebase) dataNote;
        } else {
            dataFromFirebase = new DataNoteFromFirebase(dataNote);
        }
        return dataFromFirebase;
    }

    @Override
    public void recreateList() {
        mCollections.orderBy(DataNoteFromFirebase.FIELD_DATE, Query.Direction.DESCENDING).get().
                addOnCompleteListener(this::onFetchComplete).
                addOnFailureListener(this::onFetchFailure);
    }

    @Override
    public void addAndRemoveFavoriteNote(@NonNull DataNote dataNote) {
        final DataNoteFromFirebase dataFromFirebase;
        String id = dataNote.getId();
        if (id != null) {
            int index = 0;
            for (DataNote note : mDataNotes) {
                if (id.equals(note.getId())) {
                    note.setNoteFavorite(dataNote.getNoteFavorite());
                    dataFromFirebase = initCard(dataNote);
                    mCollections.document(id).update(dataFromFirebase.getFields()).
                            addOnFailureListener((exception) -> Log.w(TAG_DEBUG, "Error updating document", exception));
                    return;
                }
                index++;
            }
        }
    }
}
