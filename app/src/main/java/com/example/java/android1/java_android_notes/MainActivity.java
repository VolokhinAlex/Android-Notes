package com.example.java.android1.java_android_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.java.android1.java_android_notes.ui.AuthFragment;
import com.example.java.android1.java_android_notes.ui.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean mIsDarkTheme;
    private boolean mIsSystemTheme;
    private int mTextSize;
    private int mLayoutView;
    public static boolean IS_LOG_OUT = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_main);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
        }

        if (savedInstanceState == null) {
            //ListOfNotesFragment fragment = new ListOfNotesFragment();
            AuthFragment fragment = new AuthFragment();
            fragment.setArguments(getIntent().getExtras());
            addFragment(fragment, false, true);
        }
        initView();
    }

    private void initView() {
        Toolbar toolbar = initToolbar();
        initDrawerMenu(toolbar);
    }

    private void initDrawerMenu(Toolbar toolbar) {
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (navigateFragment(id)) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            return false;
        });
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (navigateFragment(id)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        searchNote(menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    private boolean navigateFragment(int id) {
        switch (id) {
            case R.id.action_settings:
                addFragment(new SettingsFragment(), true, false);
                Toast.makeText(getApplicationContext(), "SETTINGS", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_about_app:
                Toast.makeText(getApplicationContext(), "ABOUT APP", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_sort:
                Toast.makeText(getApplicationContext(), "SORTED", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_logout:
                addFragment(new AuthFragment(), false, false);
                IS_LOG_OUT = true;
                return true;
        }
        return false;
    }

    private void searchNote(Menu menu) {
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Если нужно искать сразу после нажатия клавиши.
                return true;
            }
        });
    }

    public static Fragment getVisibleFragment(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        int countFragments = fragments.size();
        for (int i = countFragments - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment.isVisible())
                return fragment;
        }
        return null;
    }

    private void addFragment(Fragment fragment, boolean isBackStack, boolean isFirstStart) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        boolean isPortraitOrientation =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        if (isPortraitOrientation || isFirstStart) {
            fragmentTransaction.replace(R.id.list_of_notes_container, fragment).
                    setReorderingAllowed(true);
        } else {
            fragmentTransaction.replace(R.id.note_description_container, fragment).
                    setReorderingAllowed(true);
        }

        if (isBackStack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    private void readSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences(Settings.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        mIsDarkTheme = sharedPreferences.getBoolean(Settings.KEY_IS_DARK_THEME, false);
        mIsSystemTheme = sharedPreferences.getBoolean(Settings.KEY_IS_SYSTEM_THEME, true);
        mTextSize = sharedPreferences.getInt(Settings.KEY_TEXT_SIZE, Settings.MEDIUM_TEXT_SIZE);
        mLayoutView = sharedPreferences.getInt(Settings.KEY_LAYOUT_VIEW, Settings.LINEAR_LAYOUT_VIEW);
    }

    private void setTheme() {
        readSetting();
        if (mIsDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

        switch (mTextSize) {
            case Settings.SMALL_TEXT_SIZE:
                setTextSize(getResources().getConfiguration(), 0.7f);
                break;
            case Settings.MEDIUM_TEXT_SIZE:
                setTextSize(getResources().getConfiguration(), 1f);
                break;
            case Settings.LARGE_TEXT_SIZE:
                setTextSize(getResources().getConfiguration(), 1.5f);
                break;
        }

    }

    private void setTextSize(Configuration configuration, float scale) {
        configuration.fontScale = scale;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }

}