package com.example.java.android1.java_android_notes.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.java.android1.java_android_notes.databinding.CustomBottomSheetDialogBinding
import com.example.java.android1.java_android_notes.model.DataNote
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog(
    private var dataNote: DataNote?,
    private val onRemoveListener: ((DataNote) -> Unit)?,
) : BottomSheetDialogFragment() {

    private var _binding: CustomBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomBottomSheetDialogBinding.inflate(inflater)
        if (savedInstanceState != null) {
            dataNote = savedInstanceState.getParcelable(KEY_CURRENT_INDEX)
        }
        dataNote?.apply {
            binding.dialogTitle.text = noteTitle
            binding.dialogDescription.text = noteDescription
            binding.dialogDate.text = noteDate
        }
        binding.dialogBtnEdit.setOnClickListener {
            BottomSheetDialogEditFragment(
                dataNote = dataNote
            ).show(
                requireActivity().supportFragmentManager,
                KEY_BOTTOM_SHEET_DIALOG_EDIT_NOTE
            )
            dismiss()
        }
        binding.dialogBtnRemove.setOnClickListener {
            DialogConfirmFragment(
                dataNote = dataNote,
                onSuccessConfirmListener = onRemoveListener
            ).show(
                requireActivity().supportFragmentManager,
                KEY_DIALOG_CONFIRM_DELETE
            )
            dismiss()
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_CURRENT_INDEX, dataNote)
    }

    companion object {
        private const val KEY_CURRENT_INDEX = "BottomSheetDialog.index"
        private const val KEY_BOTTOM_SHEET_DIALOG_EDIT_NOTE =
            "ListOfNotesFragment.BottomSheetDialog.EditNote"
        private const val KEY_DIALOG_CONFIRM_DELETE = "ListOfNotesFragment.Dialog.ConfirmDelete"
    }
}