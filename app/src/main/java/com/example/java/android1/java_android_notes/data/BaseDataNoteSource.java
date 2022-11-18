package com.example.java.android1.java_android_notes.data;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseDataNoteSource implements DataNoteSource {

    private HashSet<DataNoteSourceListener> mListeners = new HashSet<>();
    protected final LinkedList<DataNote> mDataNotes = new LinkedList<>();

    @Override
    public void addChangesListener(DataNoteSourceListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void removeChangesListener(DataNoteSourceListener listener) {
        mListeners.remove(listener);
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
        int index = mDataNotes.size() - 1;
        for (DataNoteSourceListener listener : mListeners) {
            listener.onItemAdded(index);
        }
    }

    @Override
    public void removeItem(int position) {
        mDataNotes.remove(position);
        for (DataNoteSourceListener listener : mListeners) {
            listener.onDataSetChanged();
        }
    }

    protected final void notifyUpdated(int index) {
        for (DataNoteSourceListener listener : mListeners) {
            listener.onItemUpdated(index);
        }
    }

    protected final void notifyDataSetChanged() {
        for (DataNoteSourceListener listener : mListeners) {
            listener.onDataSetChanged();
        }
    }
}
