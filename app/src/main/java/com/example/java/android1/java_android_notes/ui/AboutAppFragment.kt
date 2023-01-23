package com.example.java.android1.java_android_notes.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.java.android1.java_android_notes.BuildConfig
import com.example.java.android1.java_android_notes.MainActivity
import com.example.java.android1.java_android_notes.R
import com.example.java.android1.java_android_notes.databinding.FragmentAboutAppBinding
import com.google.common.reflect.Reflection

class AboutAppFragment : Fragment() {

    private var _binding: FragmentAboutAppBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutAppBinding.inflate(inflater)
        binding.currentAppVersion.text =
            "${getString(R.string.current_app_version)}: ${BuildConfig.VERSION_NAME}"
        binding.btnWindowAppSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse(
                "package:" + Reflection.getPackageName(
                    MainActivity::class.java
                )
            )
            startActivity(intent)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}