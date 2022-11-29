package com.example.java.android1.java_android_notes.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.data.DataNote;
import com.example.java.android1.java_android_notes.data.DataNoteSource;
import com.example.java.android1.java_android_notes.listeners.OnItemClickListener;
import com.example.java.android1.java_android_notes.listeners.OnItemLongClickListener;
import com.example.java.android1.java_android_notes.ui.ListOfNotesFragment;

public class NotesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final LayoutInflater inflater;
    private final DataNoteSource mDataNoteSource;
    private OnItemClickListener mOnItemClickListener;
    private final ListOfNotesFragment mNotesFragment;
    private OnItemLongClickListener mOnItemLongClickListener;

    public NotesAdapter(ListOfNotesFragment notesFragment, DataNoteSource dataNoteSource) {
        this.mNotesFragment = notesFragment;
        this.mDataNoteSource = dataNoteSource;
        this.inflater = mNotesFragment.getLayoutInflater();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataNote dataNote = mDataNoteSource.getItem(position);
        holder.itemView.setOnClickListener((view) -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClickListener(view, position);
            }
        });
        holder.fillCard(mNotesFragment, dataNote, mOnItemLongClickListener);
        holder.setFavoriteNote(mDataNoteSource, position);
    }

    @Override
    public int getItemCount() {
        return mDataNoteSource.getDataNoteCount();
    }
}
