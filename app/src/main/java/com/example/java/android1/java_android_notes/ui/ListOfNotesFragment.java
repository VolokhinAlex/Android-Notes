package com.example.java.android1.java_android_notes.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.android1.java_android_notes.MainActivity;
import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.Settings;
import com.example.java.android1.java_android_notes.data.DataNote;
import com.example.java.android1.java_android_notes.data.DataNoteSource;
import com.example.java.android1.java_android_notes.data.DataNoteSourceFirebase;
import com.example.java.android1.java_android_notes.dialogs.BottomSheetDialog;
import com.example.java.android1.java_android_notes.dialogs.DialogAddFragment;
import com.example.java.android1.java_android_notes.service.Navigation;
import com.example.java.android1.java_android_notes.service.NotesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;
import java.util.Locale;

public class ListOfNotesFragment extends Fragment {

    public static final String KEY_NOTE_POSITION = "NotesFragment.notesPosition";
    private static final String KEY_BOTTOM_SHEET_DIALOG_NOTE = "ListOfNotesFragment.BottomSheetDialog.AboutNote";
    private static final String KEY_DIALOG_ADD_NOTE = "ListOfNotesFragment.Dialog.AddNote";

    private NotesAdapter mNotesAdapter;
    private DataNoteSource mDataNoteSource;
    private int mItemIndex = -1;
    private RecyclerView mRecyclerView;
    private Navigation mNavigation;

    @SuppressLint("NotifyDataSetChanged")
    private final DataNoteSource.DataNoteSourceListener mChangeListener = new DataNoteSource.DataNoteSourceListener() {
        @Override
        public void onItemAdded(int index) {
            if (mNotesAdapter != null) {
                mNotesAdapter.notifyItemInserted(index);
                mRecyclerView.scrollToPosition(index);
            }
        }

        @Override
        public void onItemRemoved(int index) {
            if (mNotesAdapter != null) {
                mNotesAdapter.notifyItemRemoved(index);
                mItemIndex = index - 1;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_list_of_notes, container, false);
        setHasOptionsMenu(true);
        initNotes(viewGroup);
        mNavigation = new Navigation(requireActivity().getSupportFragmentManager(),
                (MainActivity) requireActivity());
        return viewGroup;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            mItemIndex = savedInstanceState.getInt(KEY_NOTE_POSITION, -1);
        }
        addNote(view);
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
            mNavigation.addFragment(fragment, true);
        });

        mNotesAdapter.setOnItemLongClickListener(position -> new BottomSheetDialog(position).
                show(getParentFragmentManager(), KEY_BOTTOM_SHEET_DIALOG_NOTE));

        setLayoutManager();
        mDataNoteSource.addChangesListener(mChangeListener);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        searchNote(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            mDataNoteSource.sortListByDate();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void searchNote(Menu menu) {
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // ???????? ?????????? ???????????? ?????????? ?????????? ?????????????? ??????????????.
                searchFilter(newText);
                return true;
            }
        });
    }

    private void searchFilter(String text) {
        LinkedList<DataNote> filterList = new LinkedList<>();
        for (DataNote note : mDataNoteSource.getDataNote()) {
            if (note.getNoteName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                filterList.add(note);
            }
        }
        mDataNoteSource.filterList(filterList);
        if (text.trim().equals("")) {
            mDataNoteSource.recreateList();
        }
    }

    private void addNote(View view) {
        FloatingActionButton actionButton = view.findViewById(R.id.create_note);
        actionButton.setOnClickListener((click) -> new DialogAddFragment().show(requireActivity().
                getSupportFragmentManager(), KEY_DIALOG_ADD_NOTE));
    }

    public void setLastSelectedPosition(int lastSelectedPosition) {
        mItemIndex = lastSelectedPosition;
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