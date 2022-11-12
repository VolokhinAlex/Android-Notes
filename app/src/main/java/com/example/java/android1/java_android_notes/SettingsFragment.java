package com.example.java.android1.java_android_notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Set;


public class SettingsFragment extends Fragment {

    private SwitchMaterial mTurnOnDarkTheme;
    private SwitchMaterial mTurnOnSystemTheme;
    private Spinner mChooseTextSize;
    private Spinner mChooseLayoutView;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        changeThemeToDark();
        changeThemeToSystem();
        chooseTextSize();
        chooseLayoutView();
    }

    private void changeThemeToDark() {
        mTurnOnDarkTheme.setOnCheckedChangeListener((btnSwitch, isTurnOn) -> {
            if (isTurnOn) {
                Settings.isDarkTheme = isTurnOn;
                Settings.isSystemTheme = false;
                mTurnOnSystemTheme.setChecked(false);
                writeSettings();
                recreateApp();
            }
        });
    }

    private void recreateApp() {
        if (Build.VERSION.SDK_INT <= 30) {
            getActivity().recreate();
        } else {
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        }
    }

    private void changeThemeToSystem() {
        mTurnOnSystemTheme.setOnCheckedChangeListener((btnSwitch, isTurnOn) -> {
            if (isTurnOn) {
                Settings.isDarkTheme = false;
                mTurnOnDarkTheme.setChecked(false);
                Settings.isSystemTheme = isTurnOn;
                writeSettings();
                recreateApp();
            }
        });
    }

    private void chooseTextSize() {
        mChooseTextSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int idx, long l) {
                Settings.textSizeIdx = idx;
                String textSize = mChooseTextSize.getSelectedItem().toString();
                switch (textSize) {
                    case "Small":
                        Settings.textSize = Settings.SMALL_TEXT_SIZE;
                        break;
                    case "Medium":
                        Settings.textSize = Settings.MEDIUM_TEXT_SIZE;
                        break;
                    case "Large":
                        Settings.textSize = Settings.LARGE_TEXT_SIZE;
                        break;
                }
                writeSettings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void chooseLayoutView() {
        mChooseLayoutView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Settings.layoutViewIdx = i;
                String layoutView = mChooseLayoutView.getSelectedItem().toString();
                switch (layoutView) {
                    case "Grid View":
                        Settings.layoutView = Settings.GRID_LAYOUT_VIEW;
                        break;
                    case "Linear View":
                        Settings.layoutView = Settings.LINEAR_LAYOUT_VIEW;
                        break;
                }
                writeSettings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initView(View view) {
        recoverData();
        mTurnOnDarkTheme = view.findViewById(R.id.switch_turn_on_dark_theme);
        mTurnOnDarkTheme.setChecked(Settings.isDarkTheme);
        mTurnOnSystemTheme = view.findViewById(R.id.switch_turn_on_system_theme);
        mTurnOnSystemTheme.setChecked(Settings.isSystemTheme);
        mChooseTextSize = view.findViewById(R.id.change_text_size);
        mChooseTextSize.setSelection(Settings.textSizeIdx);
        mChooseLayoutView = view.findViewById(R.id.change_view);
        mChooseLayoutView.setSelection(Settings.layoutViewIdx);
    }

    private void recoverData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Settings.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Settings.textSizeIdx = sharedPreferences.getInt(Settings.KEY_TEXT_SIZE_INDEX, 1);
        Settings.layoutViewIdx = sharedPreferences.getInt(Settings.KEY_LAYOUT_VIEW_INDEX, 1);
        Settings.isDarkTheme = sharedPreferences.getBoolean(Settings.KEY_IS_DARK_THEME, false);
        Settings.isSystemTheme = sharedPreferences.getBoolean(Settings.KEY_IS_SYSTEM_THEME, true);
    }

    private void writeSettings() {
        SharedPreferences sharedPreferences = requireActivity().
                getSharedPreferences(Settings.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(Settings.KEY_IS_DARK_THEME, Settings.isDarkTheme);
        edit.putBoolean(Settings.KEY_IS_SYSTEM_THEME, Settings.isSystemTheme);
        edit.putInt(Settings.KEY_TEXT_SIZE, Settings.textSize);
        edit.putInt(Settings.KEY_LAYOUT_VIEW, Settings.layoutView);
        edit.putInt(Settings.KEY_TEXT_SIZE_INDEX, Settings.textSizeIdx);
        edit.putInt(Settings.KEY_LAYOUT_VIEW_INDEX, Settings.layoutViewIdx);
        edit.apply();
    }
}