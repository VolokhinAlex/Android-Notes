package com.example.java.android1.java_android_notes.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.java.android1.java_android_notes.R
import com.example.java.android1.java_android_notes.data.DataNoteSource
import com.example.java.android1.java_android_notes.data.DataNoteSourceFirebase.Companion.instance
import com.example.java.android1.java_android_notes.databinding.CustomConfirmDialogBinding
import com.google.android.material.snackbar.Snackbar

class DialogConfirmFragment(private var currentIndex: Int) : DialogFragment() {
    private var dataNoteSource: DataNoteSource? = null

    private var _binding: CustomConfirmDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = CustomConfirmDialogBinding.inflate(requireActivity().layoutInflater)
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX, -1)
        }
        dataNoteSource = instance
        val mDataNote = dataNoteSource?.getItem(currentIndex)
        binding.confirmDialogTitleNote
        binding.confirmDialogTitleNote.text = mDataNote?.noteName
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setTitle(R.string.confirm_dialog_title)
        dialog.setView(binding.root)
        dialog.setCancelable(false)
        binding.confirmDialogNo.setOnClickListener { dismiss() }
        binding.confirmDialogYes.setOnClickListener {
            dataNoteSource?.removeItem(currentIndex)
            requireActivity().supportFragmentManager.popBackStack()
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "Removed Note",
                Snackbar.LENGTH_SHORT
            ).show()
            dismiss()
        }
        return dialog.create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_INDEX, currentIndex)
    }

    companion object {
        private const val KEY_CURRENT_INDEX = "DialogConfirmFragment.index"
    }
}