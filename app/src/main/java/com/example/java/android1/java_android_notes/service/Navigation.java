package com.example.java.android1.java_android_notes.service;

import android.content.res.Configuration;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.java.android1.java_android_notes.MainActivity;
import com.example.java.android1.java_android_notes.R;

import java.util.List;

public class Navigation {

    private static FragmentManager mFragmentManager;
    private static MainActivity mActivity;

    public Navigation(FragmentManager fragmentManager, MainActivity activity) {
        mFragmentManager = fragmentManager;
        mActivity = activity;
    }

    private Fragment getVisibleFragment(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        int countFragments = fragments.size();
        for (int i = countFragments - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment.isVisible())
                return fragment;
        }
        return null;
    }

    public void addFragment(Fragment fragment, boolean usePopBackStack, boolean isNeedToRemove,
                            boolean isFirstStart) {
        boolean isLandScape = mActivity.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        mFragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);

        if (isNeedToRemove) {
            Fragment fragmentToRemove = getVisibleFragment(mFragmentManager);
            if (fragmentToRemove != null) {
                fragmentTransaction.remove(fragmentToRemove);
            }
        }

        if (!isLandScape || isFirstStart) {
            fragmentTransaction.replace(R.id.list_of_notes_container, fragment);
        } else {
            fragmentTransaction.replace(R.id.note_description_container, fragment);
        }

        if (usePopBackStack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();

    }

}
