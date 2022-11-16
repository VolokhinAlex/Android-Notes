package com.example.java.android1.java_android_notes.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.java.android1.java_android_notes.MainActivity;
import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.data.DataNote;
import com.example.java.android1.java_android_notes.data.DataNoteSource;
import com.example.java.android1.java_android_notes.data.DataNoteSourceImpl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteDescriptionFragment extends Fragment {

    public static final String ARG_NOTE = "NoteDescriptionFragment.note";

    private DataNote mDataNote;
    private DataNoteSource mDataNoteSource;

    private int mItemIndex = -1;

    public static NoteDescriptionFragment newInstance(int itemIndex) {
        NoteDescriptionFragment fragment = new NoteDescriptionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NOTE, itemIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItemIndex = getArguments().getInt(ARG_NOTE);
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
            mItemIndex = savedInstanceState.getInt(ARG_NOTE);
        }
        setHasOptionsMenu(true);
        mDataNoteSource = DataNoteSourceImpl.getInstance(getResources());
        mDataNote = mDataNoteSource.getItem(mItemIndex);
        if (mDataNote != null) {
            initNote(view);
            editNote(view);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.note_menu_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_remove_note) {
            mDataNoteSource.removeItem(mItemIndex);
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void initNote(View view) {
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
        outState.putInt(ARG_NOTE, mItemIndex);
    }

    private void editNote(View view) {
        FloatingActionButton actionButton = view.findViewById(R.id.edit_note);
        actionButton.setOnClickListener((click) -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragmentToRemove = MainActivity.getVisibleFragment(fragmentManager);
            if (fragmentToRemove != null) {
                fragmentTransaction.remove(fragmentToRemove);
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                fragmentTransaction.replace(R.id.list_of_notes_container, EditNoteFragment.newInstance(mItemIndex)).
                        setReorderingAllowed(true).addToBackStack(null).commit();
            } else {
                fragmentTransaction.replace(R.id.note_description_container, EditNoteFragment.newInstance(mItemIndex)).
                        setReorderingAllowed(true).addToBackStack(null).commit();
            }
        });
    }

}