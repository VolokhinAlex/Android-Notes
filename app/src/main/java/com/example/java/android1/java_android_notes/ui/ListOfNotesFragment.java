package com.example.java.android1.java_android_notes.ui;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.Settings;
import com.example.java.android1.java_android_notes.data.DataNoteSource;
import com.example.java.android1.java_android_notes.data.DataNoteSourceImpl;
import com.example.java.android1.java_android_notes.service.Navigation;
import com.example.java.android1.java_android_notes.service.NotesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListOfNotesFragment extends Fragment {

    public static final String KEY_NOTE_POSITION = "NotesFragment.notesPosition";

    private boolean mIsLandScape;
    private NotesAdapter mNotesAdapter;
    private DataNoteSource mDataNoteSource;
    private int mItemIndex;
    private RecyclerView mRecyclerView;
    private int mLastSelectedPosition = -1;
    private Navigation mNavigation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_NOTE_POSITION, mItemIndex);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            mItemIndex = savedInstanceState.getInt(KEY_NOTE_POSITION, 0);
        }
        mIsLandScape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (mIsLandScape) {
            addFragment(NoteDescriptionFragment.newInstance(mItemIndex));
        }
        addNote(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_list_of_notes, container, false);
        initNotes(viewGroup);
        mNavigation = new Navigation(getParentFragmentManager());
        return viewGroup;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initNotes(ViewGroup view) {
        mRecyclerView = view.findViewById(R.id.list_of_notes_container);
        mRecyclerView.setHasFixedSize(true);
        mDataNoteSource = DataNoteSourceImpl.getInstance(getResources());
        mNotesAdapter = new NotesAdapter(this, mDataNoteSource);
        DividerItemDecoration decoration = new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.separator));
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mNotesAdapter);
        mNotesAdapter.setOnItemClickListener((click, position) -> {
            mItemIndex = position;
            addFragment(NoteDescriptionFragment.newInstance(mItemIndex));
        });
        if (requireActivity().getSharedPreferences(Settings.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE).getInt(Settings.KEY_LAYOUT_VIEW, 1) == Settings.GRID_LAYOUT_VIEW) {
            GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 2);
            mRecyclerView.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
            mRecyclerView.setLayoutManager(layoutManager);
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_remove_note) {
            mDataNoteSource.removeItem(mItemIndex);
            mNotesAdapter.notifyDataSetChanged();
        } else if(item.getItemId() == R.id.action_favorite_note) {
            Toast.makeText(getContext(), "Added Favorite Note", Toast.LENGTH_SHORT).show();
        } else {
            return super.onContextItemSelected(item);
        }
        return true;
    }

    private void addNote(View view) {
        FloatingActionButton actionButton = view.findViewById(R.id.create_note);
        actionButton.setOnClickListener((click) -> {
            int position = mDataNoteSource.getDataNoteCount();
            addFragment(new AddNoteFragment());
            mRecyclerView.scrollToPosition(position);
        });
    }

    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!mIsLandScape) {
            fragmentTransaction.replace(R.id.list_of_notes_container, fragment)
                    .setReorderingAllowed(true).addToBackStack(null).commit();
        } else {
            fragmentTransaction.replace(R.id.note_description_container, fragment)
                    .setReorderingAllowed(true).addToBackStack(null).commit();
        }
    }

    public void setLastSelectedPosition(int lastSelectedPosition) {
        mLastSelectedPosition = lastSelectedPosition;
        mItemIndex = mLastSelectedPosition;
    }
}