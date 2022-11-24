package com.example.java.android1.java_android_notes.service;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.data.DataNote;
import com.example.java.android1.java_android_notes.data.DataNoteSource;
import com.example.java.android1.java_android_notes.listeners.OnItemLongClickListener;
import com.example.java.android1.java_android_notes.ui.ListOfNotesFragment;
import com.google.android.material.checkbox.MaterialCheckBox;

public class ViewHolder extends RecyclerView.ViewHolder {

    private final TextView text;
    private final TextView data;
    private final TextView description;
    private final MaterialCheckBox favorite;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.text = itemView.findViewById(R.id.card_item_title);
        this.data = itemView.findViewById(R.id.card_item_date);
        this.description = itemView.findViewById(R.id.card_item_description);
        this.favorite = itemView.findViewById(R.id.card_item_favorite);
    }

    public void fillCard(ListOfNotesFragment fragment, DataNote dataNote, OnItemLongClickListener longClickListener) {
        text.setText(dataNote.getNoteName());
        data.setText(dataNote.getNoteDate());
        description.setText(dataNote.getNoteDescription());
        favorite.setChecked(dataNote.getNoteFavorite());
        itemView.setOnLongClickListener((view) -> {
            fragment.setLastSelectedPosition(getLayoutPosition());
            longClickListener.onItemLongClickListener(getLayoutPosition());
            return false;
        });
    }

    public void clear(Fragment fragment) {
        itemView.setOnClickListener(null);
    }

    protected void setFavoriteNote(DataNoteSource dataNoteSource, int index) {
        favorite.setOnCheckedChangeListener((compoundButton, isFavoriteNote) -> {
            DataNote dataNote = dataNoteSource.getItem(index);
            dataNote.setNoteFavorite(isFavoriteNote);
            dataNoteSource.addAndRemoveFavoriteNote(dataNote);
            // :TODO IF a problem with updating recycler view app causes a crash, will try disabled method sortByFavorite()
            itemView.post(dataNoteSource::sortByFavorite);
        });
    }

}
