package com.example.java.android1.java_android_notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public interface DataNoteSource {

    List<DataNote> getDataNote();

    DataNote getItem(int index);

    int getDataNoteCount();

    void createItem(@NonNull DataNote dataNote);

    boolean removeItem(int position);

}
