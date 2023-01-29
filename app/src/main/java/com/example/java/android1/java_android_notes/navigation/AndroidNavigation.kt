package com.example.java.android1.java_android_notes.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.java.android1.java_android_notes.R
import com.example.java.android1.java_android_notes.view.details.DetailsNoteFragment
import com.example.java.android1.java_android_notes.view.main.MainFragment
import com.example.java.android1.java_android_notes.view.settings.SettingsFragment

class AndroidNavigation(private val fragmentManager: FragmentManager) : Screens {
    override fun notesScreen() {
        HelperNavigation.replaceFragment(MainFragment.newInstance(), false, fragmentManager)
    }

    override fun detailsNoteScreen(data: Bundle) {
        HelperNavigation.replaceFragment(
            DetailsNoteFragment.newInstance(data),
            true,
            fragmentManager
        )
    }

    override fun settingsScreen() {
        HelperNavigation.replaceFragment(SettingsFragment.newInstance(), false, fragmentManager)
    }
}

object HelperNavigation {
    fun replaceFragment(
        fragment: Fragment,
        isBackStack: Boolean,
        fragmentManager: FragmentManager
    ) {
        val transition =
            fragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.list_of_notes_container, fragment)
        if (isBackStack) {
            transition.addToBackStack(null)
        }
        transition.commit()
    }
}