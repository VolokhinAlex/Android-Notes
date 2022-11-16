package com.example.java.android1.java_android_notes.service;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.android1.java_android_notes.data.DataNote;
import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.ui.ListOfNotesFragment;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView text;
    public TextView data;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.text = itemView.findViewById(R.id.card_item_title);
        this.data = itemView.findViewById(R.id.card_item_date);
    }

    public void fillCard(ListOfNotesFragment fragment, DataNote dataNote) {
        text.setText(dataNote.getNoteName());
        data.setText(dataNote.getNoteDate());
        itemView.setOnLongClickListener((view) -> {
            fragment.setLastSelectedPosition(getLayoutPosition());
            return false;
        });
        fragment.registerForContextMenu(itemView);
    }

    public void clear(Fragment fragment) {
        itemView.setOnClickListener(null);
        fragment.unregisterForContextMenu(itemView);
    }

}
