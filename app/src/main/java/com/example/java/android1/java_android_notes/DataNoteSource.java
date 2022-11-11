package com.example.java.android1.java_android_notes;

import java.util.List;

public interface DataNoteSource {

    List<DataNote> getDataNote();

    DataNote getItem(int index);

    int getDataNoteCount();

}
