package com.example.java.android1.java_android_notes.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.java.android1.java_android_notes.R
import com.example.java.android1.java_android_notes.databinding.CustomConfirmDialogBinding
import com.example.java.android1.java_android_notes.model.DataNote

class DialogConfirmFragment(
    private var dataNote: DataNote?,
    private val onSuccessConfirmListener: ((DataNote) -> Unit)?
) : DialogFragment() {

    private var _binding: CustomConfirmDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = CustomConfirmDialogBinding.inflate(requireActivity().layoutInflater)
        if (savedInstanceState != null) {
            dataNote = savedInstanceState.getParcelable(ARG_DATA_NOTE)
        }
        binding.confirmDialogTitleNote
        binding.confirmDialogTitleNote.text = dataNote?.noteTitle
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setTitle(R.string.confirm_dialog_title)
        dialog.setView(binding.root)
        dialog.setCancelable(false)
        binding.confirmDialogNo.setOnClickListener { dismiss() }
        binding.confirmDialogYes.setOnClickListener {
            dataNote?.let { data -> onSuccessConfirmListener?.invoke(data) }
            dismiss()
        }

        //binding.confirmDialogYes.setOnClickListener {
//            dataNoteSource?.removeItem(currentIndex)
//            requireActivity().supportFragmentManager.popBackStack()
//            Snackbar.make(
//                requireActivity().findViewById(android.R.id.content),
//                "Removed Note",
//                Snackbar.LENGTH_SHORT
//            ).show()
        // }
        return dialog.create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ARG_DATA_NOTE, dataNote)
    }

    companion object {
        private const val ARG_DATA_NOTE = "DialogConfirmFragment.index"
    }
}