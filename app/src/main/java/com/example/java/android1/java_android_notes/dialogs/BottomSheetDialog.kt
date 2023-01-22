package com.example.java.android1.java_android_notes.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.java.android1.java_android_notes.data.DataNoteSource
import com.example.java.android1.java_android_notes.data.DataNoteSourceFirebase.Companion.instance
import com.example.java.android1.java_android_notes.databinding.CustomBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog(private var index: Int) : BottomSheetDialogFragment() {
    private var mDataNoteSource: DataNoteSource? = null
    private var _binding: CustomBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomBottomSheetDialogBinding.inflate(inflater)
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(KEY_CURRENT_INDEX, -1)
        }
        mDataNoteSource = instance
        val dataNote = mDataNoteSource?.getItem(index)
        dataNote?.apply {
            binding.dialogTitle.text = noteName
            binding.dialogDescription.text = noteDescription
            binding.dialogDate.text = noteDate
        }
        binding.dialogBtnEdit.setOnClickListener {
            BottomSheetDialogEditFragment(index).show(
                requireActivity().supportFragmentManager,
                KEY_BOTTOM_SHEET_DIALOG_EDIT_NOTE
            )
            dismiss()
        }
        binding.dialogBtnRemove.setOnClickListener {
            DialogConfirmFragment(index).show(
                requireActivity().supportFragmentManager,
                KEY_DIALOG_CONFIRM_DELETE
            )
            dismiss()
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_INDEX, index)
    }

    companion object {
        private const val KEY_CURRENT_INDEX = "BottomSheetDialog.index"
        private const val KEY_BOTTOM_SHEET_DIALOG_EDIT_NOTE =
            "ListOfNotesFragment.BottomSheetDialog.EditNote"
        private const val KEY_DIALOG_CONFIRM_DELETE = "ListOfNotesFragment.Dialog.ConfirmDelete"
    }
}