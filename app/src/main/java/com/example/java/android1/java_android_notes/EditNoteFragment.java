package com.example.java.android1.java_android_notes;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditNoteFragment extends Fragment {

    private DataNote mDataNote;
    private boolean isEnableDatePicker;

    public static EditNoteFragment newInstance(DataNote dataNote) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(NoteDescriptionFragment.ARG_NOTE, dataNote);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDataNote = getArguments().getParcelable(NoteDescriptionFragment.ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.edit_note_container);
        if (savedInstanceState != null) {
            mDataNote = savedInstanceState.getParcelable(NoteDescriptionFragment.ARG_NOTE);
        }
        if (mDataNote != null) {
            EditText noteTitle = view.findViewById(R.id.edit_note_title);
            EditText noteText = view.findViewById(R.id.edit_note_text);
            TextView noteDate = view.findViewById(R.id.edit_note_date);
            noteTitle.setText(mDataNote.getNoteName());
            noteText.setText(mDataNote.getNoteDescription());
            noteDate.setText(mDataNote.getNoteDate());
            MaterialButton btnEditDate = view.findViewById(R.id.changeDate);
            btnEditDate.setOnClickListener((event) -> {
                setDate(noteDate, layout);
            });
        }
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

    private void setDate(TextView noteDate, LinearLayout layout) {
        DatePicker datePicker = new DatePicker(getContext());
        if (!isEnableDatePicker) {
            layout.addView(datePicker);
        }
        isEnableDatePicker = true;
        Calendar date = getDate();
        datePicker.init(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH), (datePicker1, year, month, dayOfMonth) -> {
                    month += 1;
                    if (dayOfMonth <= 9 && month <= 9) {
                        noteDate.setText(String.format("0%s.0%s.%s", dayOfMonth, month, year));
                        mDataNote.setNoteDate(String.format("0%s.0%s.%s", dayOfMonth, month, year));
                    } else if (dayOfMonth <= 9) {
                        noteDate.setText(String.format("0%s.%s.%s", dayOfMonth, month, year));
                        mDataNote.setNoteDate(String.format("0%s.%s.%s", dayOfMonth, month, year));
                    } else if (month <= 9) {
                        noteDate.setText(String.format("%s.0%s.%s", dayOfMonth, month, year));
                        mDataNote.setNoteDate(String.format("%s.0%s.%s", dayOfMonth, month, year));
                    } else {
                        noteDate.setText(String.format("%s.%s.%s", dayOfMonth, month, year));
                        mDataNote.setNoteDate(String.format("%s.%s.%s", dayOfMonth, month, year));
                    }
                    layout.removeView(datePicker);
                    isEnableDatePicker = false;
                });
    }
}