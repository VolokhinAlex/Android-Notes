package com.example.java.android1.java_android_notes.view.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.java.android1.java_android_notes.databinding.FragmentAddNoteBinding
import com.example.java.android1.java_android_notes.model.DataNote
import java.text.SimpleDateFormat
import java.util.*

class DialogAddFragment(private val onEventListener: ((DataNote) -> Unit)?) : DialogFragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SimpleDateFormat")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentAddNoteBinding.inflate(requireActivity().layoutInflater)
        if (savedInstanceState != null) {
            binding.addNoteTitle.setText(savedInstanceState.getString(KEY_NOTE_TITLE))
            binding.addNoteText.setText(savedInstanceState.getString(KEY_NOTE_DESCRIPTION))
        }
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Add Note").setView(binding.root).setCancelable(false)
        val format = SimpleDateFormat("dd.MM.yyyy")
        binding.btnAddNote.setOnClickListener {
            if (binding.addNoteTitle.text.toString().trim { it <= ' ' } != "") {
                onEventListener?.invoke(
                    DataNote(
                        id = 0,
                        noteTitle = binding.addNoteTitle.text.toString(),
                        noteDescription = binding.addNoteText.text.toString(),
                        noteFavorite = false,
                        noteDate = format.format(Date())
                    )
                )
                dismiss()
            }
        }
        return builder.create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_NOTE_TITLE, binding.addNoteTitle.text.toString())
        outState.putString(KEY_NOTE_DESCRIPTION, binding.addNoteText.text.toString())
    }

    companion object {
        private const val KEY_NOTE_TITLE = "DialogAddFragment.Note.title"
        private const val KEY_NOTE_DESCRIPTION = "DialogAddFragment.Note.title"
    }
}