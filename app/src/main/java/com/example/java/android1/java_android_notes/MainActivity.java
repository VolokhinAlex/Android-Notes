package com.example.java.android1.java_android_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
        }

        if (savedInstanceState == null) {
            NoteDescriptionFragment fragment = new NoteDescriptionFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .replace(R.id.note_description, fragment).commit();
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
                addFragment(new SettingsFragment());
                Toast.makeText(getApplicationContext(), "SETTINGS", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.about_app:
                Toast.makeText(getApplicationContext(), "ABOUT APP", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_sort:
                Toast.makeText(getApplicationContext(), "SORTED", Toast.LENGTH_SHORT).show();
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

    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragmentToRemove = getVisibleFragment(fragmentManager);
        if (fragmentToRemove != null) {
            fragmentTransaction.remove(fragmentToRemove);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentTransaction.replace(R.id.notes_list, fragment).
                    setReorderingAllowed(true).addToBackStack(null).commit();
        } else {
            fragmentTransaction.replace(R.id.note_description, fragment).
                    setReorderingAllowed(true).addToBackStack(null).commit();
        }
    }

}