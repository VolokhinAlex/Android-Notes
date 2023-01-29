package com.example.java.android1.java_android_notes.view.settings

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.java.android1.java_android_notes.BuildConfig
import com.example.java.android1.java_android_notes.R
import com.example.java.android1.java_android_notes.Settings
import com.example.java.android1.java_android_notes.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater)
        initView()
        chooseTextSize()
        chooseLayoutView()
        saveSettings()
        return binding.root
    }

    private fun recreateApp() {
        if (Build.VERSION.SDK_INT <= 30) {
            requireActivity().recreate()
        } else {
            val intent = requireActivity().intent
            requireActivity().finish()
            startActivity(intent)
        }
    }

    private fun chooseTextSize() {
        binding.changeTextSize.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    idx: Int,
                    l: Long
                ) {
                    Settings.textSizeIdx = idx
                    when (binding.changeTextSize.selectedItem.toString()) {
                        "Small" -> Settings.textSize = Settings.SMALL_TEXT_SIZE
                        "Medium" -> Settings.textSize = Settings.MEDIUM_TEXT_SIZE
                        "Large" -> Settings.textSize = Settings.LARGE_TEXT_SIZE
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
    }

    private fun chooseLayoutView() {
        binding.changeView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                Settings.layoutViewIdx = i
                when (binding.changeView.selectedItem.toString()) {
                    "Grid View" -> Settings.layoutView = Settings.GRID_LAYOUT_VIEW
                    "Linear View" -> Settings.layoutView = Settings.LINEAR_LAYOUT_VIEW
                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun saveSettings() {
        binding.btnSaveSettings.setOnClickListener {
            writeSettings()
            recreateApp()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        recoverSettings()
        binding.changeTextSize.setSelection(Settings.textSizeIdx)
        binding.changeView.setSelection(Settings.layoutViewIdx)
        binding.currentAppVersion.text =
            "${getString(R.string.current_app_version)}: ${BuildConfig.VERSION_NAME}"
    }

    private fun recoverSettings() {
        val sharedPreferences = requireActivity().getSharedPreferences(
            Settings.SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        Settings.textSizeIdx = sharedPreferences.getInt(Settings.KEY_TEXT_SIZE_INDEX, 1)
        Settings.layoutViewIdx = sharedPreferences.getInt(Settings.KEY_LAYOUT_VIEW_INDEX, 1)
    }

    private fun writeSettings() {
        val sharedPreferences = requireActivity().getSharedPreferences(
            Settings.SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        val edit = sharedPreferences.edit()
        edit.putInt(Settings.KEY_TEXT_SIZE, Settings.textSize)
        edit.putInt(Settings.KEY_LAYOUT_VIEW, Settings.layoutView)
        edit.putInt(Settings.KEY_TEXT_SIZE_INDEX, Settings.textSizeIdx)
        edit.putInt(Settings.KEY_LAYOUT_VIEW_INDEX, Settings.layoutViewIdx)
        edit.apply()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}