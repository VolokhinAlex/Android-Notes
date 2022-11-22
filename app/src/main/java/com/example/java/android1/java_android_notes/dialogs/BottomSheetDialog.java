package com.example.java.android1.java_android_notes.dialogs;

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

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private static final String KEY_CURRENT_INDEX = "BottomSheetDialog.index";
    private static final String KEY_BOTTOM_SHEET_DIALOG_EDIT_NOTE = "ListOfNotesFragment.BottomSheetDialog.EditNote";
    private static final String KEY_DIALOG_CONFIRM_DELETE = "ListOfNotesFragment.Dialog.ConfirmDelete";
    private int mIndex;
    private DataNoteSource mDataNoteSource;

    public BottomSheetDialog() {

    }

    public BottomSheetDialog(int index) {
        mIndex = index;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_bottom_sheet_dialog, container, false);
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX, -1);
        }
        mDataNoteSource = DataNoteSourceFirebase.getInstance();
        DataNote dataNote = mDataNoteSource.getItem(mIndex);
        MaterialButton btnEditData = view.findViewById(R.id.dialog_btn_edit);
        MaterialButton btnRemoveData = view.findViewById(R.id.dialog_btn_remove);
        TextView tvTitleNote = view.findViewById(R.id.dialog_title);
        TextView tvDescriptionNote = view.findViewById(R.id.dialog_description);
        TextView tvDateNote = view.findViewById(R.id.dialog_date);

        tvTitleNote.setText(dataNote.getNoteName());
        tvDescriptionNote.setText(dataNote.getNoteDescription());
        tvDateNote.setText(dataNote.getNoteDate());

        btnEditData.setOnClickListener((viewClick) -> {
            new BottomSheetDialogEditFragment(mIndex).show(requireActivity().getSupportFragmentManager(), KEY_BOTTOM_SHEET_DIALOG_EDIT_NOTE);
            dismiss();
        });

        btnRemoveData.setOnClickListener((viewClick) -> {
            new DialogConfirmFragment(mIndex).show(requireActivity().getSupportFragmentManager(), KEY_DIALOG_CONFIRM_DELETE);
            dismiss();
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_INDEX, mIndex);
    }
}
