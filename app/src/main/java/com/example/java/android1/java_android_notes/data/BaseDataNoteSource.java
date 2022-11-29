package com.example.java.android1.java_android_notes.data;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
            listener.onItemRemoved(position);
        }
    }

    protected final void notifyAdded(int index) {
        for (DataNoteSourceListener listener : mListeners) {
            listener.onItemAdded(index);
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

    public void filterList(LinkedList<DataNote> dataNotes) {
        mDataNotes.clear();
        mDataNotes.addAll(dataNotes);
        notifyDataSetChanged();
    }

    @SuppressLint("SimpleDateFormat")
    public void sortListByDate() {
        LinkedList<DataNote> dataNotes = new LinkedList<>(mDataNotes);
        Collections.sort(dataNotes, (dataNote, t1) -> {
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            try {
                return Objects.requireNonNull(format.parse(dataNote.getNoteDate())).
                        compareTo(format.parse(t1.getNoteDate()));
            } catch (ParseException e) {
                return 0;
            }
        });
        mDataNotes.clear();
        mDataNotes.addAll(dataNotes);
        dataNotes.clear();
        notifyDataSetChanged();
    }

    public void sortByFavorite() {
        LinkedList<DataNote> dataNotes = new LinkedList<>(mDataNotes);
        Collections.sort(dataNotes, (dataNote, dataNote2) ->
                Boolean.compare(!dataNote.getNoteFavorite(),
                !dataNote2.getNoteFavorite()));
        mDataNotes.clear();
        mDataNotes.addAll(dataNotes);
        dataNotes.clear();
        notifyDataSetChanged();
    }

}
