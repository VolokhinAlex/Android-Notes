package com.example.java.android1.java_android_notes;

import android.content.Intent;
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

public class NotesFragment extends Fragment {

    public static final String KEY_NOTE_POSITION = "NotesFragment.notesPosition";

    private DataNotes mCurrentNote;
    private boolean mIsLandScape;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_NOTE_POSITION, mCurrentNote);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNotes(view);
        if (savedInstanceState != null) {
            mCurrentNote = savedInstanceState.getParcelable(KEY_NOTE_POSITION);
        }
        mIsLandScape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (mIsLandScape) {
            showNoteOnLandOrientation(mCurrentNote);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    private void initNotes(View view) {
        LinearLayout layout = (LinearLayout) view;
        String[] notesName = getResources().getStringArray(R.array.notes_name_list);
        String[] notesDescription = getResources().getStringArray(R.array.notes_description_list);
        String[] notesDate = getResources().getStringArray(R.array.notes_date_list);
        for (int i = 0; i < notesName.length; i++) {
            final int index = i;
            TextView note = new TextView(getContext());
            note.setText(notesName[i]);
            note.setTextSize(18);
            layout.addView(note);
            note.setOnClickListener((v) -> {
                mCurrentNote = new DataNotes(index, notesName[index], notesDescription[index],
                        notesDate[index]);
                chooseOrientation(mCurrentNote);
            });
        }
    }

    private void chooseOrientation(DataNotes currentNote) {
        if (mIsLandScape) {
            showNoteOnLandOrientation(currentNote);
        } else {
            showNoteOnPortraitOrientation(currentNote);
        }
    }

    private void showNoteOnPortraitOrientation(DataNotes currentNote) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NoteDescriptionActivity.class);
        intent.putExtra(NoteDescriptionFragment.ARG_NOTE, currentNote);
        startActivity(intent);
    }

    private void showNoteOnLandOrientation(DataNotes currentNote) {
        NoteDescriptionFragment fragment = NoteDescriptionFragment.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.note_description, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}