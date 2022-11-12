package com.example.java.android1.java_android_notes;

import java.util.List;

public interface DataNoteSource {

    List<DataNote> getDataNote();

    DataNote getItem(int index);

    int getDataNoteCount();

    void createItem(String title, String text, String date);

    boolean removeItem(int position);

}
