package com.example.java.android1.java_android_notes.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.example.java.android1.java_android_notes.Settings
import com.example.java.android1.java_android_notes.databinding.ActivityMainBinding
import com.example.java.android1.java_android_notes.navigation.AndroidNavigation

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var textSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            AndroidNavigation(supportFragmentManager).notesScreen()
        }
    }

    private fun readSetting() {
        val sharedPreferences = getSharedPreferences(Settings.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
        textSize = sharedPreferences.getInt(Settings.KEY_TEXT_SIZE, Settings.MEDIUM_TEXT_SIZE)
    }

    private fun setTheme() {
        readSetting()
        when (textSize) {
            Settings.SMALL_TEXT_SIZE -> setTextSize(
                resources.configuration, 0.7f
            )
            Settings.MEDIUM_TEXT_SIZE -> setTextSize(
                resources.configuration, 1f
            )
            Settings.LARGE_TEXT_SIZE -> setTextSize(
                resources.configuration, 1.5f
            )
        }
    }

    @Suppress("DEPRECATION")
    private fun setTextSize(configuration: Configuration, scale: Float) {
        configuration.fontScale = scale
        val metrics = resources.displayMetrics
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = display
            display?.getRealMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        } else {
            val windowManager = getSystemService<WindowManager>()
            windowManager?.defaultDisplay?.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
    }
}