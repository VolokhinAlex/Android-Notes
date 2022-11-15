package com.example.java.android1.java_android_notes;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DataNoteSourceImpl implements DataNoteSource {

    private final LinkedList<DataNote> mDataNotes = new LinkedList<>();

    public DataNoteSourceImpl(Resources resources) {
        String[] notesName = resources.getStringArray(R.array.notes_name_list);
        String[] notesDescription = resources.getStringArray(R.array.notes_description_list);
        String[] notesDate = resources.getStringArray(R.array.notes_date_list);
        for (int i = 0; i < notesName.length; i++) {
            mDataNotes.add(new DataNote(i, notesName[i], notesDescription[i], notesDate[i]));
        }
    }

    @Override
    public List<DataNote> getDataNote() {
        return Collections.unmodifiableList(mDataNotes);
    }

    @Override
    public DataNote getItem(int index) {
        return mDataNotes.get(index);
    }

    @Override
    public int getDataNoteCount() {
        return mDataNotes.size();
    }

    @Override
    public void createItem(@NonNull DataNote dataNote) {
        mDataNotes.add(dataNote);
    }

    @Override
    public boolean removeItem(int position) {
        mDataNotes.remove(position);
        return true;
    }
}
