package com.example.java.android1.java_android_notes.view.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.example.java.android1.java_android_notes.R
import com.example.java.android1.java_android_notes.Settings
import com.example.java.android1.java_android_notes.databinding.FragmentMainBinding
import com.example.java.android1.java_android_notes.model.DataNote
import com.example.java.android1.java_android_notes.navigation.AndroidNavigation
import com.example.java.android1.java_android_notes.utils.convertNoteEntityListToDataNoteList
import com.example.java.android1.java_android_notes.view.details.DetailsNoteFragment
import com.example.java.android1.java_android_notes.view.dialogs.BottomSheetDialog
import com.example.java.android1.java_android_notes.view.dialogs.DialogAddFragment
import com.example.java.android1.java_android_notes.viewmodel.MainViewModel
import com.example.java.android1.java_android_notes.viewmodel.MainViewModelFactory
import java.util.*


class MainFragment : Fragment() {

    private val navigation: AndroidNavigation by lazy {
        AndroidNavigation(requireActivity().supportFragmentManager)
    }
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory())[MainViewModel::class.java]
    }
    private val notesAdapter: NotesAdapter by lazy {
        NotesAdapter(
            onLongClickListener = { note ->
                openSheetDialog(dataNote = note)
            }, onItemClickListener = { note ->
                navigation.detailsNoteScreen(
                    bundleOf(Pair(DetailsNoteFragment.ARG_DETAILS_NOTE, note))
                )
            }, onCheckedChangeListener = { note, isChecked ->
                updateFavoriteValueNote(dataNote = note, isChecked = isChecked)
            })
    }

    private var notesListData: List<DataNote> = listOf()

    private fun openSheetDialog(dataNote: DataNote) {
        BottomSheetDialog(dataNote = dataNote) { mainViewModel.removeNote(it) }.show(
            parentFragmentManager,
            ""
        )
    }

    private fun updateFavoriteValueNote(dataNote: DataNote, isChecked: Boolean) {
        mainViewModel.upsertNote(
            DataNote(
                id = dataNote.id,
                noteTitle = dataNote.noteTitle,
                noteDescription = dataNote.noteDescription,
                noteFavorite = isChecked,
                noteDate = dataNote.noteDate
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        initNotesRecyclerView()
        setHasOptionsMenu(true)
        mainViewModel.notesData.observe(viewLifecycleOwner) {
            val notes = convertNoteEntityListToDataNoteList(it)
            notesAdapter.setNotesList(notes)
            notesListData = notes
        }
        binding.createNote.setOnClickListener {
            DialogAddFragment(onEventListener = {
                mainViewModel.upsertNote(it)
            }).show(requireActivity().supportFragmentManager, "")
        }
        return binding.root
    }

    private fun initNotesRecyclerView() {
        binding.listOfNotesContainer.layoutManager = layoutManager()
        binding.listOfNotesContainer.adapter = notesAdapter
        setAnimation(1000)
        setDecoration()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val menuInflater = requireActivity().menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        searchNote(menu)
    }

    private fun layoutManager(): RecyclerView.LayoutManager =
        if (requireActivity().getSharedPreferences(
                Settings.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE
            ).getInt(Settings.KEY_LAYOUT_VIEW, 1) == Settings.GRID_LAYOUT_VIEW
        ) {
            GridLayoutManager(requireActivity(), 2)
        } else {
            LinearLayoutManager(requireActivity())
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            navigation.settingsScreen()
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
                mainViewModel.searchChangedQuery(newText.lowercase())
                return true
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

}
