package com.example.java.android1.java_android_notes;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NoteDescriptionFragment extends Fragment {

    public static final String ARG_NOTE = "NoteDescriptionFragment.note";

    private DataNotes mDataNotes;
    private boolean isEnableDatePicker;

    public static NoteDescriptionFragment newInstance(DataNotes note) {
        NoteDescriptionFragment fragment = new NoteDescriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDataNotes = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            mDataNotes = savedInstanceState.getParcelable(ARG_NOTE);
        }
        initNote(view);
    }

    private void initNote(View view) {
        LinearLayout layout = (LinearLayout) view;
        TextView noteName = generateTextView(mDataNotes.getNoteName(), 20,
                0, 0, 0, 0);
        TextView noteDescription = generateTextView(mDataNotes.getNoteDescription(), 20,
                0, 0, 0, 25);
        TextView noteDate = generateTextView(mDataNotes.getNoteDate(), 20,
                0, 0, 0, 30);
        MaterialButton editDate = new MaterialButton(getContext());
        editDate.setText(R.string.button_change_date);
        editDate.setOnClickListener((event) -> {
            setDate(noteDate, layout);
        });
        layout.addView(noteName);
        layout.addView(noteDescription);
        layout.addView(noteDate);
        layout.addView(editDate);
    }

    private TextView generateTextView(String text, int textSize, int leftPadding,
                                      int rightPadding, int bottomPadding, int topPadding) {
        TextView view = new TextView(getContext());
        view.setText(text);
        view.setTextSize(textSize);
        view.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_NOTE, mDataNotes);
    }

    @SuppressLint("SimpleDateFormat")
    private Calendar getDate() {
        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_format));
        try {
            format.parse(mDataNotes.getNoteDate());
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
                        mDataNotes.setNoteDate(String.format("0%s.0%s.%s", dayOfMonth, month, year));
                    } else if (dayOfMonth <= 9) {
                        noteDate.setText(String.format("0%s.%s.%s", dayOfMonth, month, year));
                        mDataNotes.setNoteDate(String.format("0%s.%s.%s", dayOfMonth, month, year));
                    } else if (month <= 9) {
                        noteDate.setText(String.format("%s.0%s.%s", dayOfMonth, month, year));
                        mDataNotes.setNoteDate(String.format("%s.0%s.%s", dayOfMonth, month, year));
                    } else {
                        noteDate.setText(String.format("%s.%s.%s", dayOfMonth, month, year));
                        mDataNotes.setNoteDate(String.format("%s.%s.%s", dayOfMonth, month, year));
                    }
                    layout.removeView(datePicker);
                    isEnableDatePicker = false;
                });
    }
}