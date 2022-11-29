package com.example.java.android1.java_android_notes.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.data.DataNote;
import com.example.java.android1.java_android_notes.data.DataNoteSource;
import com.example.java.android1.java_android_notes.data.DataNoteSourceFirebase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteFragment extends Fragment {

    private static final String ARG_ITEM_INDEX = "ItemIndex";

    private TextInputEditText mEditTitle;
    private TextInputEditText mEditText;
    private MaterialButton mBtnAddNote;
    private DataNoteSource mDataNoteSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        mDataNoteSource = DataNoteSourceFirebase.getInstance();
        addNewNote();
    }

    private void initViews(View view) {
        mEditTitle = view.findViewById(R.id.add_note_title);
        mEditText = view.findViewById(R.id.add_note_text);
        mBtnAddNote = view.findViewById(R.id.btn_add_note);
    }

    @SuppressLint("SimpleDateFormat")
    private void addNewNote() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        mBtnAddNote.setOnClickListener((view) -> {
            if (!mEditTitle.getText().toString().trim().equals("")) {
                mDataNoteSource.createItem(new DataNote(mEditTitle.getText().toString(),
                        mEditText.getText().toString(), false, format.format(new Date())));
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

}