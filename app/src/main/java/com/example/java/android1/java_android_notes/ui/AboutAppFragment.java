package com.example.java.android1.java_android_notes.ui;

import static com.google.common.reflect.Reflection.getPackageName;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.java.android1.java_android_notes.BuildConfig;
import com.example.java.android1.java_android_notes.MainActivity;
import com.example.java.android1.java_android_notes.R;
import com.google.android.material.button.MaterialButton;

public class AboutAppFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_about_app, container, false);
        TextView tv = view.findViewById(R.id.current_app_version);
        String versionName = BuildConfig.VERSION_NAME;
        MaterialButton btnOpenWindowSettings = view.findViewById(R.id.btn_window_app_settings);
        btnOpenWindowSettings.setOnClickListener((v) -> {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName(MainActivity.class)));
            startActivity(intent);
        });
        tv.setText(String.format("%s: %s", getString(R.string.current_app_version), versionName));
        return view;
    }
}