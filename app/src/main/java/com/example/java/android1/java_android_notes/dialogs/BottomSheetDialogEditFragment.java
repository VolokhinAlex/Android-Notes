package com.example.java.android1.java_android_notes.dialogs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.data.DataNote;
import com.example.java.android1.java_android_notes.data.DataNoteSource;
import com.example.java.android1.java_android_notes.data.DataNoteSourceFirebase;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BottomSheetDialogEditFragment extends BottomSheetDialogFragment {

    private static final String KEY_CURRENT_INDEX = "BottomSheetDialogEditFragment.index";
    private TextInputEditText mEditTitle;
    private TextInputEditText mEditText;
    private TextView mTvDate;
    private DatePickerDialog mDatePickerDialog;
    private DataNote mDataNote;
    private DataNoteSource mDataNoteSource;

    private int mIndex;

    public BottomSheetDialogEditFragment() {

    }

    public BottomSheetDialogEditFragment(int index) {
        mIndex = index;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX, -1);
        }
        mDataNoteSource = DataNoteSourceFirebase.getInstance();
        mEditTitle = view.findViewById(R.id.edit_note_title);
        mEditText = view.findViewById(R.id.edit_note_text);
        mTvDate = view.findViewById(R.id.edit_note_date);
        mDataNote = mDataNoteSource.getItem(mIndex);
        mEditTitle.setText(mDataNote.getNoteName());
        mEditText.setText(mDataNote.getNoteDescription());
        mTvDate.setText(mDataNote.getNoteDate());
        MaterialButton btnEditDate = view.findViewById(R.id.btn_change_date);
        btnEditDate.setOnClickListener((event) -> setDate());
        onSaveData(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_INDEX, mIndex);
    }

    private void onSaveData(@NonNull View view) {
        MaterialButton btnSaveData = view.findViewById(R.id.btn_save_data);
        btnSaveData.setOnClickListener((event) -> {
            mDataNote.setNoteName(mEditTitle.getText().toString());
            mDataNote.setNoteDescription(mEditText.getText().toString());
            mDataNote.setNoteDate(mTvDate.getText().toString());
            mDataNoteSource.updateItem(mDataNote);
            dismiss();
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
