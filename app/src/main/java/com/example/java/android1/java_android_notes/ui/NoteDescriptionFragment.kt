package com.example.java.android1.java_android_notes.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.java.android1.java_android_notes.MainActivity
import com.example.java.android1.java_android_notes.R
import com.example.java.android1.java_android_notes.data.DataNote
import com.example.java.android1.java_android_notes.data.DataNoteSource
import com.example.java.android1.java_android_notes.data.DataNoteSourceFirebase
import com.example.java.android1.java_android_notes.databinding.FragmentNoteDescriptionBinding
import com.example.java.android1.java_android_notes.dialogs.DialogConfirmFragment
import com.example.java.android1.java_android_notes.service.Navigation

class NoteDescriptionFragment : Fragment() {
    private var _binding: FragmentNoteDescriptionBinding? = null
    private val binding get() = _binding!!

    private var mDataNote: DataNote? = null
    private var mDataNoteSource: DataNoteSource? = null
    private var mItemIndex = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mItemIndex = requireArguments().getInt(ARG_NOTE, -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDescriptionBinding.inflate(inflater)
        if (savedInstanceState != null) {
            mItemIndex = savedInstanceState.getInt(ARG_NOTE, -1)
        }
        setHasOptionsMenu(true)
        mDataNoteSource = DataNoteSourceFirebase.instance
        mDataNoteSource?.let {
            if (mItemIndex == -1 || mItemIndex >= it.dataNoteCount) {
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                mDataNote = mDataNoteSource?.getItem(mItemIndex)
                if (mDataNote != null) {
                    initNote()
                    editNote()
                }
            }
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_menu_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_remove_note) {
            DialogConfirmFragment(mItemIndex).show(
                requireActivity().supportFragmentManager,
                KEY_DIALOG_CONFIRM_DELETE
            )
        } else {
            return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun initNote() {
        mDataNote?.apply {
            binding.itemTitle.text = noteName
            binding.itemText.text = noteDescription
            binding.itemDate.text = noteDate
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ARG_NOTE, mItemIndex)
    }

    private fun editNote() {
        binding.editNote.setOnClickListener {
            val navigation = Navigation(
                requireActivity().supportFragmentManager,
                (requireActivity() as MainActivity)
            )
            navigation.addFragment(EditNoteFragment.newInstance(mItemIndex), true)
        }
    }

    companion object {
        const val ARG_NOTE = "NoteDescriptionFragment.note"
        private const val KEY_DIALOG_CONFIRM_DELETE = "ListOfNotesFragment.Dialog.ConfirmDelete"

        @JvmStatic
        fun newInstance(itemIndex: Int) =
            NoteDescriptionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_NOTE, itemIndex)
                }
            }
    }
}