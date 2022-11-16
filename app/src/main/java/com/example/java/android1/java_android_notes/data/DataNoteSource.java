package com.example.java.android1.java_android_notes.data;

import androidx.annotation.NonNull;

import java.util.List;

public interface DataNoteSource {

    List<DataNote> getDataNote();

    DataNote getItem(int index);

    int getDataNoteCount();

    void createItem(@NonNull DataNote dataNote);

    void removeItem(int position);

}
