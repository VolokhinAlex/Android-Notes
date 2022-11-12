package com.example.java.android1.java_android_notes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.android1.java_android_notes.DataNote;
import com.example.java.android1.java_android_notes.DataNoteSource;
import com.example.java.android1.java_android_notes.R;

public class NotesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final LayoutInflater inflater;
    private final DataNoteSource mDataNoteSource;
    private OnItemClickListener mOnItemClickListener;
    private OnItemCreateContextMenuListener mOnItemCreateContextMenuListener;

    public NotesAdapter(LayoutInflater inflater, DataNoteSource dataNoteSource) {
        this.inflater = inflater;
        this.mDataNoteSource = dataNoteSource;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemCreateContextMenuListener(OnItemCreateContextMenuListener onItemCreateContextMenuListener) {
        this.mOnItemCreateContextMenuListener = onItemCreateContextMenuListener;
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
        holder.itemView.setOnCreateContextMenuListener((contextMenu, view, contextMenuInfo) ->
                mOnItemCreateContextMenuListener.onItemCreateContextMenuListener(contextMenu, view, contextMenuInfo, position));
        holder.fillCard(dataNote);
    }

    @Override
    public int getItemCount() {
        return mDataNoteSource.getDataNoteCount();
    }
}
