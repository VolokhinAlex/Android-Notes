package com.example.java.android1.java_android_notes.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.data.DataNote;
import com.example.java.android1.java_android_notes.data.DataNoteSource;
import com.example.java.android1.java_android_notes.data.DataNoteSourceFirebase;
import com.google.android.material.button.MaterialButton;

public class DialogConfirmFragment extends DialogFragment {

    private static final String KEY_CURRENT_INDEX = "DialogConfirmFragment.index";
    private DataNoteSource mDataNoteSource;
    private int mCurrentIndex;

    public DialogConfirmFragment() {

    }

    public DialogConfirmFragment(int currentIndex) {
        mCurrentIndex = currentIndex;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = requireActivity().getLayoutInflater().inflate(R.layout.custom_confirm_dialog, null, false);
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX, -1);
        }
        mDataNoteSource = DataNoteSourceFirebase.getInstance();
        DataNote mDataNote = mDataNoteSource.getItem(mCurrentIndex);
        TextView title = view.findViewById(R.id.confirm_dialog_title_note);
        MaterialButton btnNo = view.findViewById(R.id.confirm_dialog_no);
        MaterialButton btnYes = view.findViewById(R.id.confirm_dialog_yes);
        title.setText(mDataNote.getNoteName());
        Log.e("TAG_CHECKER", String.valueOf(mCurrentIndex));
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());

        dialog.setTitle(R.string.confirm_dialog_title);
        dialog.setView(view);
        dialog.setCancelable(false);

        btnNo.setOnClickListener((click) -> {
            dismiss();
        });

        btnYes.setOnClickListener((click) -> {
            mDataNoteSource.removeItem(mCurrentIndex);
            dismiss();
        });

        return dialog.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_INDEX, mCurrentIndex);
    }
}
