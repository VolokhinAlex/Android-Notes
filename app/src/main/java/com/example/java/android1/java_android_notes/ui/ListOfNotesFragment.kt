package com.example.java.android1.java_android_notes.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.example.java.android1.java_android_notes.MainActivity
import com.example.java.android1.java_android_notes.R
import com.example.java.android1.java_android_notes.Settings
import com.example.java.android1.java_android_notes.data.DataNote
import com.example.java.android1.java_android_notes.data.DataNoteSource
import com.example.java.android1.java_android_notes.data.DataNoteSource.DataNoteSourceListener
import com.example.java.android1.java_android_notes.data.DataNoteSourceFirebase
import com.example.java.android1.java_android_notes.databinding.FragmentListOfNotesBinding
import com.example.java.android1.java_android_notes.dialogs.BottomSheetDialog
import com.example.java.android1.java_android_notes.dialogs.DialogAddFragment
import com.example.java.android1.java_android_notes.service.Navigation
import com.example.java.android1.java_android_notes.service.NotesAdapter
import java.util.*

class ListOfNotesFragment : Fragment() {
    private var dataNoteSource: DataNoteSource? = null
    private var itemIndex = -1
    private var navigation: Navigation? = null
    private val notesAdapter: NotesAdapter by lazy {
        NotesAdapter(
            notesFragment = this,
            dataNoteSource = dataNoteSource,
            onItemClickListener = { position ->
                itemIndex = position
                val fragment: NoteDescriptionFragment =
                    NoteDescriptionFragment.newInstance(itemIndex)
                navigation?.addFragment(fragment, true)
            },
            onItemLongClickListener = { position ->
                BottomSheetDialog(position).show(
                    parentFragmentManager, KEY_BOTTOM_SHEET_DIALOG_NOTE
                )
            })
    }

    private var _binding: FragmentListOfNotesBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("NotifyDataSetChanged")
    private val changeListener: DataNoteSourceListener = object : DataNoteSourceListener {
        override fun onItemAdded(index: Int) {
            notesAdapter.notifyItemInserted(index)
            binding.listOfNotesContainer.scrollToPosition(index)
        }

        override fun onItemRemoved(index: Int) {
            notesAdapter.notifyItemRemoved(index)
            itemIndex = index - 1
        }

        override fun onItemUpdated(index: Int) {
            notesAdapter.notifyItemChanged(index)
        }

        override fun onDataSetChanged() {
            notesAdapter.notifyDataSetChanged()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_NOTE_POSITION, itemIndex)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOfNotesBinding.inflate(inflater)
        setHasOptionsMenu(true)
        initNotes()
        navigation = Navigation(
            requireActivity().supportFragmentManager,
            (requireActivity() as MainActivity)
        )
        if (savedInstanceState != null) {
            itemIndex = savedInstanceState.getInt(KEY_NOTE_POSITION, -1)
        }
        addNote()
        return binding.root
    }

    private fun initNotes() {
        dataNoteSource = DataNoteSourceFirebase.instance
        binding.listOfNotesContainer.apply {
            setHasFixedSize(true)
            adapter = notesAdapter
        }
        setDecoration()
        setAnimation(1000)
        setLayoutManager()
        dataNoteSource?.addChangesListener(changeListener)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val menuInflater = requireActivity().menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        searchNote(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_sort) {
            dataNoteSource?.sortListByDate()
        } else {
            return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun searchNote(menu: Menu) {
        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchFilter(newText)
                return true
            }
        })
    }

    private fun searchFilter(text: String) {
        val filterList = LinkedList<DataNote>()
        dataNoteSource?.dataNote?.forEach { dataNote ->
            if (dataNote.noteName?.lowercase()?.contains(text.lowercase()) == true) {
                filterList.add(dataNote)
            }
        }
        dataNoteSource?.filterList(filterList)
        if (text.trim { it <= ' ' } == "") {
            dataNoteSource?.recreateList()
        }
    }

    private fun addNote() {
        binding.createNote.setOnClickListener {
            DialogAddFragment().show(
                requireActivity().supportFragmentManager,
                KEY_DIALOG_ADD_NOTE
            )
        }
    }

    fun setLastSelectedPosition(lastSelectedPosition: Int) {
        itemIndex = lastSelectedPosition
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataNoteSource?.removeChangesListener(changeListener)
    }

    private fun setLayoutManager() {
        val isGridLayoutView = requireActivity().getSharedPreferences(
            Settings.SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        ).getInt(Settings.KEY_LAYOUT_VIEW, 1) == Settings.GRID_LAYOUT_VIEW
        if (isGridLayoutView) {
            val layoutManager = GridLayoutManager(requireActivity(), 2)
            binding.listOfNotesContainer.layoutManager = layoutManager
        } else {
            val layoutManager = LinearLayoutManager(requireActivity())
            binding.listOfNotesContainer.layoutManager = layoutManager
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setDecoration() {
        val decoration = DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.separator))
        binding.listOfNotesContainer.addItemDecoration(decoration)
    }

    private fun setAnimation(duration: Long) {
        val animator = DefaultItemAnimator()
        animator.addDuration = duration
        animator.removeDuration = duration
        binding.listOfNotesContainer.itemAnimator = animator
    }

    companion object {
        const val KEY_NOTE_POSITION = "NotesFragment.notesPosition"
        private const val KEY_BOTTOM_SHEET_DIALOG_NOTE =
            "ListOfNotesFragment.BottomSheetDialog.AboutNote"
        private const val KEY_DIALOG_ADD_NOTE = "ListOfNotesFragment.Dialog.AddNote"
    }
}