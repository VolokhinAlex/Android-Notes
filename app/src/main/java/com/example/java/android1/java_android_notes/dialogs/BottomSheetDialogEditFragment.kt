package com.example.java.android1.java_android_notes.dialogs

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.java.android1.java_android_notes.R
import com.example.java.android1.java_android_notes.data.DataNote
import com.example.java.android1.java_android_notes.data.DataNoteSourceFirebase.Companion.instance
import com.example.java.android1.java_android_notes.databinding.FragmentEditNoteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BottomSheetDialogEditFragment(private var index: Int) : BottomSheetDialogFragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private var dataNote: DataNote? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater)
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(KEY_CURRENT_INDEX, -1)
        }
        val dataNoteSource = instance
        dataNote = dataNoteSource?.getItem(index)
        binding.editNoteTitle.setText(dataNote?.noteName)
        binding.editNoteText.setText(dataNote?.noteDescription)
        binding.editNoteDate.text = dataNote?.noteDate
        binding.btnChangeDate.setOnClickListener { setDate() }
        onSaveData()
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_INDEX, index)
    }

    private fun onSaveData() {
        dataNote?.apply {
            binding.btnSaveData.setOnClickListener {
                noteName = binding.editNoteTitle.text.toString()
                noteDescription = binding.editNoteText.text.toString()
                noteDate = binding.editNoteDate.text.toString()
                val dataNoteSource = instance
                dataNoteSource?.updateItem(this)
                dismiss()
            }
        }
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
        private const val KEY_CURRENT_INDEX = "BottomSheetDialogEditFragment.index"
    }
}