package com.example.java.android1.java_android_notes.service

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.java.android1.java_android_notes.MainActivity
import com.example.java.android1.java_android_notes.R

class Navigation(
    private val mFragmentManager: FragmentManager,
    private val mActivity: MainActivity
) {
    fun addFragment(fragment: Fragment?, usePopBackStack: Boolean) {
        val fragmentTransaction: FragmentTransaction = mFragmentManager.beginTransaction()
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.replace(R.id.list_of_notes_container, fragment!!)
        if (usePopBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    companion object
}