package com.example.java.android1.java_android_notes.data;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

public interface DataNoteSource {

    interface DataNoteSourceListener {
        void onItemAdded(int index);
        void onItemUpdated(int index);
        void onDataSetChanged();
    }

    List<DataNote> getDataNote();
    DataNote getItem(int index);
    int getDataNoteCount();

    void createItem(@NonNull DataNote dataNote);
    void removeItem(int position);
    void updateItem(@NonNull DataNote dataNote);

    void addChangesListener(DataNoteSourceListener listener);
    void removeChangesListener(DataNoteSourceListener listener);

    void filterList(LinkedList<DataNote> dataNotes);
    void recreateList();
}
