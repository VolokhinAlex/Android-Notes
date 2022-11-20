package com.example.java.android1.java_android_notes;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.java.android1.java_android_notes.data.DataAuth;
import com.example.java.android1.java_android_notes.service.Navigation;
import com.example.java.android1.java_android_notes.ui.AboutAppFragment;
import com.example.java.android1.java_android_notes.ui.AuthFragment;
import com.example.java.android1.java_android_notes.ui.SettingsFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_GOOGLE_DATA = "GoogleAuthSaveData";

    private boolean mIsDarkTheme;
    private int mTextSize;
    public static boolean IS_LOG_OUT = false;
    private Navigation mNavigationToFragment;
    private DataAuth mDataAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_main);
        mNavigationToFragment = new Navigation(getSupportFragmentManager(), this);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
        }
        if (savedInstanceState == null) {
            AuthFragment fragment = new AuthFragment();
            fragment.setArguments(getIntent().getExtras());
            mNavigationToFragment.addFragment(fragment, false, false, true);
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

    @SuppressLint("NonConstantResourceId")
    private boolean navigateFragment(int id) {
        switch (id) {
            case R.id.action_settings:
                mNavigationToFragment.addFragment(new SettingsFragment(), true,
                        false, false);
                return true;
            case R.id.action_about_app:
                mNavigationToFragment.addFragment(new AboutAppFragment(), true,
                        false, false);
                return true;
            case R.id.action_logout:
                mNavigationToFragment.addFragment(new AuthFragment(), false, true, false);
                IS_LOG_OUT = true;
                return true;
        }
        return false;
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

    private void readSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences(Settings.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        mIsDarkTheme = sharedPreferences.getBoolean(Settings.KEY_IS_DARK_THEME, false);
        boolean isSystemTheme = sharedPreferences.getBoolean(Settings.KEY_IS_SYSTEM_THEME, true);
        mTextSize = sharedPreferences.getInt(Settings.KEY_TEXT_SIZE, Settings.MEDIUM_TEXT_SIZE);
        int layoutView = sharedPreferences.getInt(Settings.KEY_LAYOUT_VIEW, Settings.LINEAR_LAYOUT_VIEW);
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

    public void setDataAuth(DataAuth dataAuth) {
        this.mDataAuth = dataAuth;
        setData();
    }

    private void setData() {
        if (mDataAuth != null) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);

            TextView userName = headerView.findViewById(R.id.user_name);
            TextView userEmail = headerView.findViewById(R.id.user_email);
            ShapeableImageView imageView = headerView.findViewById(R.id.image_avatar);
            if (mDataAuth.getImageProfile() != null) {
                Picasso.get().load(mDataAuth.getImageProfile()).into(imageView);
            }
            userName.setText(mDataAuth.getFullName());
            userEmail.setText(mDataAuth.getEmail());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_GOOGLE_DATA, mDataAuth);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mDataAuth = savedInstanceState.getParcelable(KEY_GOOGLE_DATA);
        setData();
    }

}