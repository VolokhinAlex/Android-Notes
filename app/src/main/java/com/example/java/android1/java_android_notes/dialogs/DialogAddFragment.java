package com.example.java.android1.java_android_notes.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.data.DataNote;
import com.example.java.android1.java_android_notes.data.DataNoteSource;
import com.example.java.android1.java_android_notes.data.DataNoteSourceFirebase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogAddFragment extends DialogFragment {

    private static final String KEY_NOTE_TITLE = "DialogAddFragment.Note.title";
    private static final String KEY_NOTE_DESCRIPTION = "DialogAddFragment.Note.title";

    private TextInputEditText mEditTitle;
    private TextInputEditText mEditText;

    public DialogAddFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = requireActivity().getLayoutInflater().inflate(R.layout.fragment_add_note, null, false);

        mEditTitle = view.findViewById(R.id.add_note_title);
        mEditText = view.findViewById(R.id.add_note_text);
        MaterialButton materialButton = view.findViewById(R.id.btn_add_note);
        DataNoteSource dataNoteSource = DataNoteSourceFirebase.getInstance();
        if (savedInstanceState != null) {
            mEditTitle.setText(savedInstanceState.getString(KEY_NOTE_TITLE));
            mEditText.setText(savedInstanceState.getString(KEY_NOTE_DESCRIPTION));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Add Note").setView(view).setCancelable(false);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        materialButton.setOnClickListener((v) -> {
            if (!mEditTitle.getText().toString().trim().equals("")) {
                dataNoteSource.createItem(new DataNote(mEditTitle.getText().toString(),
                        mEditText.getText().toString(), false, format.format(new Date())));
                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_NOTE_TITLE, mEditTitle.getText().toString());
        outState.putString(KEY_NOTE_DESCRIPTION, mEditText.getText().toString());
    }
}
