package com.example.java.android1.java_android_notes.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.example.java.android1.java_android_notes.R
import com.example.java.android1.java_android_notes.data.DataNote
import com.example.java.android1.java_android_notes.data.DataNoteSourceFirebase
import com.example.java.android1.java_android_notes.databinding.FragmentEditNoteBinding
import com.example.java.android1.java_android_notes.ui.NoteDescriptionFragment.Companion.ARG_NOTE
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class EditNoteFragment : Fragment() {
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private var mDataNote: DataNote? = null
    private var mItemIndex = 0
    private val mDataNoteSource by lazy {
        DataNoteSourceFirebase.instance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mItemIndex = requireArguments().getInt(NoteDescriptionFragment.Companion.ARG_NOTE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater)
        if (savedInstanceState != null) {
            mItemIndex = savedInstanceState.getInt(KEY_ITEM_INDEX)
        }
        mDataNote = mDataNoteSource?.getItem(mItemIndex)
        mDataNote?.let {
            initView()
            onSaveData()
            binding.btnChangeDate.setOnClickListener { setDate() }
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_ITEM_INDEX, mItemIndex)
    }

    private fun initView() {
        mDataNote?.apply {
            binding.editNoteTitle.setText(noteName)
            binding.editNoteText.setText(noteDescription)
            binding.editNoteDate.text = noteDate
        }
    }

    private fun onSaveData() {
        binding.btnSaveData.setOnClickListener {
            mDataNote?.apply {
                noteName = binding.editNoteTitle.text.toString()
                noteDescription = binding.editNoteText.text.toString()
                noteDate = binding.editNoteDate.text.toString()
                mDataNoteSource?.updateItem(this)
            }
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private val date: Calendar
        @SuppressLint("SimpleDateFormat")
        get() {
            val format = SimpleDateFormat(getString(R.string.date_format))
            try {
                mDataNote?.noteDate?.let { format.parse(it) }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return format.calendar
        }

    @SuppressLint("SimpleDateFormat")
    private fun setDate() {
        val format = SimpleDateFormat("dd.MM.yyyy")
        DatePickerDialog(
            requireActivity(),
            { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                date[year, monthOfYear] = dayOfMonth
                binding.editNoteDate.text = format.format(date.time)
            },
            date[Calendar.YEAR],
            date[Calendar.MONTH],
            date[Calendar.DAY_OF_MONTH]
        ).show()
    }

    companion object {
        private const val KEY_ITEM_INDEX = "EditNoteFragment.KeyItemIndex"

        @JvmStatic
        fun newInstance(itemIndex: Int) =
            EditNoteFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_NOTE, itemIndex)
                }
            }
    }
}