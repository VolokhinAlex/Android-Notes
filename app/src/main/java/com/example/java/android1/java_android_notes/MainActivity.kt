package com.example.java.android1.java_android_notes

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.java.android1.java_android_notes.data.DataAuth
import com.example.java.android1.java_android_notes.databinding.ActivityMainBinding
import com.example.java.android1.java_android_notes.service.Navigation
import com.example.java.android1.java_android_notes.ui.AboutAppFragment
import com.example.java.android1.java_android_notes.ui.AuthFragment
import com.example.java.android1.java_android_notes.ui.ListOfNotesFragment
import com.example.java.android1.java_android_notes.ui.SettingsFragment
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private var isDarkTheme = false
    private var textSize = 0
    private var navigationToFragment: Navigation? = null
    private var auth: DataAuth? = null
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationToFragment = Navigation(
            supportFragmentManager, this
        )
        if (savedInstanceState == null) {
            val fragment = ListOfNotesFragment()
            fragment.arguments = intent.extras
            navigationToFragment?.addFragment(fragment, false)
        }
        initView()
    }

    private fun initView() {
        val toolbar = initToolbar()
        initDrawerMenu(toolbar)
    }

    private fun initDrawerMenu(toolbar: Toolbar) {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            val id = item.itemId
            if (navigateFragment(id)) {
                drawerLayout.closeDrawer(GravityCompat.START)
                return@setNavigationItemSelectedListener true
            }
            false
        }
    }

    private fun initToolbar(): Toolbar {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        return toolbar
    }

    @SuppressLint("NonConstantResourceId")
    private fun navigateFragment(id: Int): Boolean {
        when (id) {
            R.id.action_settings -> {
                navigationToFragment?.addFragment(SettingsFragment(), true)
                return true
            }
            R.id.action_about_app -> {
                navigationToFragment?.addFragment(AboutAppFragment(), true)
                return true
            }
            R.id.action_logout -> {
                navigationToFragment?.addFragment(AuthFragment(), false)
                IS_LOG_OUT = true
                return true
            }
            android.R.id.home -> {
                supportFragmentManager.popBackStack()
                return true
            }
        }
        return false
    }

    private fun readSetting() {
        val sharedPreferences = getSharedPreferences(Settings.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
        isDarkTheme = sharedPreferences.getBoolean(Settings.KEY_IS_DARK_THEME, false)
        textSize = sharedPreferences.getInt(Settings.KEY_TEXT_SIZE, Settings.MEDIUM_TEXT_SIZE)
    }

    private fun setTheme() {
        readSetting()
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
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
            val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
    }

    fun setDataAuth(dataAuth: DataAuth?) {
        auth = dataAuth
        setData()
    }

    private fun setData() {
        if (auth != null) {
            val headerView = binding.navView.getHeaderView(0)
            val userName = headerView.findViewById<TextView>(R.id.user_name)
            val userEmail = headerView.findViewById<TextView>(R.id.user_email)
            val imageView = headerView.findViewById<ShapeableImageView>(R.id.image_avatar)
            auth?.let {
                if (it.imageProfile != null) {
                    Picasso.get().load(it.imageProfile).into(imageView)
                }
                userName.text = it.fullName
                userEmail.text = it.email
            }
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_GOOGLE_DATA, auth)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        auth = savedInstanceState.getParcelable(KEY_GOOGLE_DATA)
        setData()
    }

    companion object {
        private const val KEY_GOOGLE_DATA = "GoogleAuthSaveData"
        @JvmField
        var IS_LOG_OUT = false
    }
}