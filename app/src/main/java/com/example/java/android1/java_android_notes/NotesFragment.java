package com.example.java.android1.java_android_notes;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.java.android1.java_android_notes.ui.NotesAdapter;
import com.example.java.android1.java_android_notes.ui.ViewHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesFragment extends Fragment {

    public static final String KEY_NOTE_POSITION = "NotesFragment.notesPosition";

    private DataNote mCurrentNote;
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
        addNote(view);
        if (savedInstanceState != null) {
            mCurrentNote = savedInstanceState.getParcelable(KEY_NOTE_POSITION);
        }
        mIsLandScape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (mIsLandScape) {
            showNoteOnLandOrientation(mCurrentNote);
        } else {
            popToBackStack();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_notes, container, false);
        initNotes(viewGroup, inflater);
        return viewGroup;
    }

    private void initNotes(ViewGroup view, LayoutInflater inflater) {
        RecyclerView recyclerView = view.findViewById(R.id.notes_list_container);
        recyclerView.setHasFixedSize(true);
        DataNoteSourceImpl dataNoteSource = new DataNoteSourceImpl(getResources());
        NotesAdapter notesAdapter = new NotesAdapter(inflater, dataNoteSource);
        DividerItemDecoration decoration = new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(notesAdapter);
        notesAdapter.setOnItemClickListener((click, position) -> {
            mCurrentNote = dataNoteSource.getItem(position);
            chooseOrientation(mCurrentNote);
        });
        notesAdapter.setOnItemCreateContextMenuListener((ContextMenu contextMenu, View view1, ContextMenu.ContextMenuInfo contextMenuInfo) -> {
            MenuInflater inflater1 = getActivity().getMenuInflater();
            inflater1.inflate(R.menu.context_menu, contextMenu);
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove_note:
                Toast.makeText(getContext(), "Removed Note", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_favorite_note:
                Toast.makeText(getContext(), "Added Favorite Note", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void chooseOrientation(DataNote currentNote) {
        if (mIsLandScape) {
            showNoteOnLandOrientation(currentNote);
        } else {
            showNoteOnPortraitOrientation(currentNote);
        }
    }

    private void showNoteOnPortraitOrientation(DataNote currentNote) {
        NoteDescriptionFragment fragment = NoteDescriptionFragment.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true).replace(R.id.notes_list, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    private void popToBackStack() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    private void showNoteOnLandOrientation(DataNote currentNote) {
        NoteDescriptionFragment fragment = NoteDescriptionFragment.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true).replace(R.id.note_description, fragment).
                addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).
                commit();
    }

    private void addNote(View view) {
        FloatingActionButton actionButton = view.findViewById(R.id.add_new_note);
        actionButton.setOnClickListener((click) -> {
            Toast.makeText(getContext(), "Add New Note", Toast.LENGTH_SHORT).show();
        });
    }
}