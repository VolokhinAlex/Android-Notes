package com.example.java.android1.java_android_notes.view.dialogs

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.java.android1.java_android_notes.R
import com.example.java.android1.java_android_notes.databinding.FragmentEditNoteBinding
import com.example.java.android1.java_android_notes.model.DataNote
import com.example.java.android1.java_android_notes.viewmodel.EditNoteViewModel
import com.example.java.android1.java_android_notes.viewmodel.EditNoteViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BottomSheetDialogEditFragment(
    private var dataNote: DataNote?,
) : BottomSheetDialogFragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private val editNoteViewModel by lazy {
        ViewModelProvider(this, EditNoteViewModelFactory())[EditNoteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater)
        if (savedInstanceState != null) {
            dataNote = savedInstanceState.getParcelable(ARG_DATA_NOTE)
        }
        binding.editNoteTitle.setText(dataNote?.noteTitle)
        binding.editNoteText.setText(dataNote?.noteDescription)
        binding.editNoteDate.text = dataNote?.noteDate
        binding.btnChangeDate.setOnClickListener { setDate() }
        binding.btnSaveData.setOnClickListener {
            editNoteViewModel.upsertNote(
                DataNote(
                    id = dataNote?.id,
                    noteTitle = binding.editNoteTitle.text.toString(),
                    noteDescription = binding.editNoteText.text.toString(),
                    noteDate = binding.editNoteDate.text.toString(),
                    noteFavorite = dataNote?.noteFavorite
                )
            )
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ARG_DATA_NOTE, dataNote)
    }

    private val date: Calendar
        @SuppressLint("SimpleDateFormat")
        get() {
            val format = SimpleDateFormat(getString(R.string.date_format))
            try {
                dataNote?.noteDate?.let { format.parse(it) }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return format.calendar
        }

    @SuppressLint("SimpleDateFormat")
    private fun setDate() {
        val format = SimpleDateFormat("dd.MM.yyyy")
        val date = date
        DatePickerDialog(
            requireActivity(),
            { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                date[year, monthOfYear] = dayOfMonth
                binding.editNoteDate.text = format.format(date.time)
            },
            date[Calendar.YEAR],
            date[Calendar.MONTH],
            date[Calendar.DAY_OF_MONTH]
        ).show()
    }

    companion object {
        private const val ARG_DATA_NOTE = "BottomSheetDialogEditFragment.index"
    }
}