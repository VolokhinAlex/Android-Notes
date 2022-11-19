package com.example.java.android1.java_android_notes.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.android1.java_android_notes.MainActivity;
import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.Settings;
import com.example.java.android1.java_android_notes.data.DataNoteSource;
import com.example.java.android1.java_android_notes.data.DataNoteSourceFirebase;
import com.example.java.android1.java_android_notes.service.Navigation;
import com.example.java.android1.java_android_notes.service.NotesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListOfNotesFragment extends Fragment {

    public static final String KEY_NOTE_POSITION = "NotesFragment.notesPosition";

    private boolean mIsLandScape;
    private NotesAdapter mNotesAdapter;
    private DataNoteSource mDataNoteSource;
    private int mItemIndex = -1;
    private RecyclerView mRecyclerView;
    private int mLastSelectedPosition = -1;
    private Navigation mNavigation;

    private final DataNoteSource.DataNoteSourceListener mChangeListener = new DataNoteSource.DataNoteSourceListener() {
        @Override
        public void onItemAdded(int index) {
            if (mNotesAdapter != null) {
                mNotesAdapter.notifyItemInserted(index);
                mRecyclerView.scrollToPosition(index);
            }
        }

        @Override
        public void onItemUpdated(int index) {
            if (mNotesAdapter != null) {
                mNotesAdapter.notifyItemChanged(index);
            }
        }

        @Override
        public void onDataSetChanged() {
            if (mNotesAdapter != null) {
                mNotesAdapter.notifyDataSetChanged();
            }
        }
    };

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
            mItemIndex = savedInstanceState.getInt(KEY_NOTE_POSITION, -1);
        }
        mIsLandScape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (mIsLandScape) {
            NoteDescriptionFragment fragment = NoteDescriptionFragment.newInstance(mItemIndex);
            mNavigation.addFragment(fragment, true, false, false);
            fragment.setOnItemChanges(mChangeListener);
        }
        addNote(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_list_of_notes, container, false);
        initNotes(viewGroup);
        mNavigation = new Navigation(requireActivity().getSupportFragmentManager(),
                (MainActivity) requireActivity());
        return viewGroup;
    }

    private void initNotes(ViewGroup view) {
        mRecyclerView = view.findViewById(R.id.list_of_notes_container);
        mRecyclerView.setHasFixedSize(true);

        mDataNoteSource = DataNoteSourceFirebase.getInstance();
        mNotesAdapter = new NotesAdapter(this, mDataNoteSource);
        setDecoration();
        setAnimation(1000);
        mRecyclerView.setAdapter(mNotesAdapter);

        mNotesAdapter.setOnItemClickListener((click, position) -> {
            mItemIndex = position;
            NoteDescriptionFragment fragment = NoteDescriptionFragment.newInstance(mItemIndex);
            mNavigation.addFragment(fragment, true, false, false);
            fragment.setOnItemChanges(mChangeListener);
        });

        setLayoutManager();
        mDataNoteSource.addChangesListener(mChangeListener);
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
        } else if (item.getItemId() == R.id.action_favorite_note) {
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
            AddNoteFragment noteFragment = new AddNoteFragment();
            noteFragment.setOnItemChanges(mChangeListener);
            mNavigation.addFragment(noteFragment, true, false, false);
            mNotesAdapter.notifyItemInserted(position);
            mRecyclerView.scrollToPosition(position);
        });
    }

    public void setLastSelectedPosition(int lastSelectedPosition) {
        mLastSelectedPosition = lastSelectedPosition;
        mItemIndex = mLastSelectedPosition;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDataNoteSource.removeChangesListener(mChangeListener);
    }

    private void setLayoutManager() {
        boolean isGridLayoutView = requireActivity().getSharedPreferences(Settings.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE).getInt(Settings.KEY_LAYOUT_VIEW, 1) == Settings.GRID_LAYOUT_VIEW;

        if (isGridLayoutView) {
            GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 2);
            mRecyclerView.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
            mRecyclerView.setLayoutManager(layoutManager);
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setDecoration() {
        DividerItemDecoration decoration = new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.separator));
        mRecyclerView.addItemDecoration(decoration);
    }

    private void setAnimation(long duration) {
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(duration);
        animator.setRemoveDuration(duration);
        mRecyclerView.setItemAnimator(animator);
    }

}