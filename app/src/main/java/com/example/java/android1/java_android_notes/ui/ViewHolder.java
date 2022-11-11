package com.example.java.android1.java_android_notes.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.android1.java_android_notes.DataNote;
import com.example.java.android1.java_android_notes.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView text;
    public TextView data;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.text = itemView.findViewById(R.id.note_item_title);
        this.data = itemView.findViewById(R.id.note_item_date);
    }

    public void fillCard(DataNote dataNote) {
        text.setText(dataNote.getNoteName());
        data.setText(dataNote.getNoteDate());
    }

}
