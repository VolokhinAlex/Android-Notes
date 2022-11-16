package com.example.java.android1.java_android_notes.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.data.DataNote;
import com.example.java.android1.java_android_notes.data.DataNoteSource;
import com.example.java.android1.java_android_notes.data.DataNoteSourceImpl;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditNoteFragment extends Fragment {

    private DataNote mDataNote;
    private int mItemIndex;
    private static final String KEY_ITEM_INDEX = "EditNoteFragment.KeyItemIndex";

    private TextInputEditText mEditTitle;
    private TextInputEditText mEditText;
    private TextView mTvDate;
    private DatePickerDialog mDatePickerDialog;

    public static EditNoteFragment newInstance(int itemIndex) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putInt(NoteDescriptionFragment.ARG_NOTE, itemIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItemIndex = getArguments().getInt(NoteDescriptionFragment.ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_ITEM_INDEX, mItemIndex);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout layout = view.findViewById(R.id.edit_note_container);
        if (savedInstanceState != null) {
            mItemIndex = savedInstanceState.getInt(KEY_ITEM_INDEX);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        }
        DataNoteSource dataNoteSource = DataNoteSourceImpl.getInstance(getResources());
        mDataNote = dataNoteSource.getItem(mItemIndex);
        if (mDataNote != null) {
            initView(view);
            onSaveData(view);
            MaterialButton btnEditDate = view.findViewById(R.id.btn_change_date);
            btnEditDate.setOnClickListener((event) -> setDate());
        }
    }

    private void initView(@NonNull View view) {
        mEditTitle = (TextInputEditText) view.findViewById(R.id.edit_note_title);
        mEditText = (TextInputEditText) view.findViewById(R.id.edit_note_text);
        mTvDate = view.findViewById(R.id.edit_note_date);
        mEditTitle.setText(mDataNote.getNoteName());
        mEditText.setText(mDataNote.getNoteDescription());
        mTvDate.setText(mDataNote.getNoteDate());
    }

    private void onSaveData(@NonNull View view) {
        MaterialButton btnSaveData = view.findViewById(R.id.btn_save_data);
        btnSaveData.setOnClickListener((event) -> {
            mDataNote.setNoteName(mEditTitle.getText().toString());
            mDataNote.setNoteDescription(mEditText.getText().toString());
            mDataNote.setNoteDate(mTvDate.getText().toString());
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    @SuppressLint("SimpleDateFormat")
    private Calendar getDate() {
        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_format));
        try {
            format.parse(mDataNote.getNoteDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.getCalendar();
    }

    @SuppressLint("SimpleDateFormat")
    private void setDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Calendar date = getDate();
        mDatePickerDialog = new DatePickerDialog(requireActivity(),
                (datePicker, year, monthOfYear, dayOfMonth) -> {
                    date.set(year, monthOfYear, dayOfMonth);
                    mTvDate.setText(format.format(date.getTime()));
                },
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.show();
    }
}