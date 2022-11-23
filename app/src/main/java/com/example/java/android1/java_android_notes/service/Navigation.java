package com.example.java.android1.java_android_notes.service;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.java.android1.java_android_notes.MainActivity;
import com.example.java.android1.java_android_notes.R;

public class Navigation {

    private static FragmentManager mFragmentManager;
    private static MainActivity mActivity;

    public Navigation(FragmentManager fragmentManager, MainActivity activity) {
        mFragmentManager = fragmentManager;
        mActivity = activity;
    }

    public void addFragment(Fragment fragment, boolean usePopBackStack) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.list_of_notes_container, fragment);
        if (usePopBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

}
