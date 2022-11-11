package com.example.java.android1.java_android_notes;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteDescriptionFragment extends Fragment {

    public static final String ARG_NOTE = "NoteDescriptionFragment.note";

    private DataNote mDataNote;

    public static NoteDescriptionFragment newInstance(DataNote note) {
        NoteDescriptionFragment fragment = new NoteDescriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDataNote = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            mDataNote = savedInstanceState.getParcelable(ARG_NOTE);
        }
        if (mDataNote != null) {
            initNote(view);
            editNote(view);
        }
    }

    private void initNote(View view) {
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.note_description);
        TextView noteName = generateTextView(view, R.id.item_title, mDataNote.getNoteName());
        TextView noteDescription = generateTextView(view, R.id.item_text, mDataNote.getNoteDescription());
        TextView noteDate = generateTextView(view, R.id.item_date, mDataNote.getNoteDate());
    }

    private TextView generateTextView(View view, int elementId, String text) {
        TextView textView = view.findViewById(elementId);
        textView.setText(text);
        return textView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_NOTE, mDataNote);
    }

    private void editNote(View view) {
        FloatingActionButton actionButton = view.findViewById(R.id.edit_note);
        actionButton.setOnClickListener((click) -> {
            Toast.makeText(getContext(), "Edit Note", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragmentToRemove = MainActivity.getVisibleFragment(fragmentManager);
            if (fragmentToRemove != null) {
                fragmentTransaction.remove(fragmentToRemove);
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                fragmentTransaction.replace(R.id.notes_list, EditNoteFragment.newInstance(mDataNote)).
                        setReorderingAllowed(true).addToBackStack(null).commit();
            } else {
                fragmentTransaction.replace(R.id.note_description, EditNoteFragment.newInstance(mDataNote)).
                        setReorderingAllowed(true).addToBackStack(null).commit();
            }
        });
    }

}