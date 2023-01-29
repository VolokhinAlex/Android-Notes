package com.example.java.android1.java_android_notes.view.details

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.java.android1.java_android_notes.R
import com.example.java.android1.java_android_notes.databinding.FragmentDetailsNoteBinding
import com.example.java.android1.java_android_notes.model.DataNote
import com.example.java.android1.java_android_notes.viewmodel.EditNoteViewModel
import com.example.java.android1.java_android_notes.viewmodel.EditNoteViewModelFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DetailsNoteFragment : Fragment() {
    private var _binding: FragmentDetailsNoteBinding? = null
    private val binding get() = _binding!!
    private var dataNote: DataNote? = null

    private val editNoteViewModel by lazy {
        ViewModelProvider(this, EditNoteViewModelFactory())[EditNoteViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dataNote = it.getParcelable(ARG_DETAILS_NOTE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsNoteBinding.inflate(inflater)

        dataNote?.apply {
            binding.itemTitle.setText(noteTitle)
            binding.itemText.setText(noteDescription)
            binding.itemDate.text = noteDate
        }

        binding.saveNote.setOnClickListener {
            editNoteViewModel.upsertNote(
                dataNote = DataNote(
                    id = dataNote?.id,
                    noteTitle = binding.itemTitle.text.toString(),
                    noteDescription = binding.itemText.text.toString(),
                    noteFavorite = dataNote?.noteFavorite,
                    noteDate = binding.itemDate.text.toString()
                )
            )
        }
        binding.itemDate.setOnClickListener { setDate() }
        return binding.root
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
                binding.itemDate.text = format.format(date.time)
            },
            date[Calendar.YEAR],
            date[Calendar.MONTH],
            date[Calendar.DAY_OF_MONTH]
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_DETAILS_NOTE = "DetailsNote"

        @JvmStatic
        fun newInstance(bundle: Bundle) =
            DetailsNoteFragment().apply {
                arguments = bundle
            }
    }
}