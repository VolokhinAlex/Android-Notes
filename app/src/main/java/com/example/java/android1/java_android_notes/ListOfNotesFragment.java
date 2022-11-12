package com.example.java.android1.java_android_notes;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListOfNotesFragment extends Fragment {

    public static final String KEY_NOTE_POSITION = "NotesFragment.notesPosition";

    private DataNote mCurrentNote;
    private boolean mIsLandScape;
    private NotesAdapter mNotesAdapter;
    private DataNoteSourceImpl mDataNoteSource;

    private int mItemIndex;

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
            addFragment(mCurrentNote, mIsLandScape);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_list_of_notes, container, false);
        initNotes(viewGroup, inflater);
        return viewGroup;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initNotes(ViewGroup view, LayoutInflater inflater) {
        RecyclerView recyclerView = view.findViewById(R.id.list_of_notes_container);
        recyclerView.setHasFixedSize(true);
        mDataNoteSource = new DataNoteSourceImpl(getResources());
        mNotesAdapter = new NotesAdapter(inflater, mDataNoteSource);
        DividerItemDecoration decoration = new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(mNotesAdapter);
        mNotesAdapter.setOnItemClickListener((click, position) -> {
            mCurrentNote = mDataNoteSource.getItem(position);
            addFragment(mCurrentNote, mIsLandScape);
        });
        mNotesAdapter.setOnItemCreateContextMenuListener(
                (ContextMenu contextMenu, View view1, ContextMenu.ContextMenuInfo contextMenuInfo, int position) -> {
                    MenuInflater inflater1 = requireActivity().getMenuInflater();
                    inflater1.inflate(R.menu.context_menu, contextMenu);
                    mItemIndex = position;
                });

        if (requireActivity().getSharedPreferences(Settings.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE).getInt(Settings.KEY_LAYOUT_VIEW, 1) == Settings.GRID_LAYOUT_VIEW) {
            GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 2);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove_note:
                Toast.makeText(getContext(), "Removed Note", Toast.LENGTH_SHORT).show();
                mDataNoteSource.removeItem(mItemIndex);
                mNotesAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_favorite_note:
                Toast.makeText(getContext(), "Added Favorite Note", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void addNote(View view) {
        FloatingActionButton actionButton = view.findViewById(R.id.create_note);
        actionButton.setOnClickListener((click) -> {
            Toast.makeText(getContext(), "Add New Note", Toast.LENGTH_SHORT).show();
            mDataNoteSource.createItem("Check", "Test Created Task", "11.12.22");
            mNotesAdapter.notifyDataSetChanged();
        });
    }

    private void addFragment(DataNote currentNote, boolean isLandScape) {
        Fragment fragmentToReplace = NoteDescriptionFragment.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!isLandScape) {
            fragmentTransaction.replace(R.id.list_of_notes, fragmentToReplace)
                    .setReorderingAllowed(true).addToBackStack(null).commit();
        } else {
            fragmentTransaction.replace(R.id.note_description, fragmentToReplace)
                    .setReorderingAllowed(true).addToBackStack(null).commit();
        }
    }

}