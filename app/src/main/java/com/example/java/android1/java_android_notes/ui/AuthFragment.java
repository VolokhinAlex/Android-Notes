package com.example.java.android1.java_android_notes.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.java.android1.java_android_notes.MainActivity;
import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.data.DataAuth;
import com.example.java.android1.java_android_notes.data.DataAuthSource;
import com.example.java.android1.java_android_notes.data.DataAuthSourceImpl;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AuthFragment extends Fragment {

    private static final String TAG = "GoogleAuth";

    private GoogleSignInClient mSignInClient;
    private SignInButton mBtnSignIn;
    private DataAuthSource mDataAuthSource;
    private DrawerLayout mDrawerLayout;
    private final ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            AuthFragment.this.handleSignInResult(task);
        }
    });


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);
        mDrawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        mDataAuthSource = DataAuthSourceImpl.getInstance();
        initGoogleSign();
        initView(view);
        enableSign();
        if (MainActivity.IS_LOG_OUT) {
            logOut();
            MainActivity.IS_LOG_OUT = false;
        }
        return view;
    }

    private void initGoogleSign() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);
    }

    private void initView(View view) {
        mBtnSignIn = view.findViewById(R.id.btn_sign_in);
        mBtnSignIn.setOnClickListener((v) -> signIn());
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (account != null) {
            mDataAuthSource.createItem(new DataAuth(account.getEmail(), account.getDisplayName(), account.getPhotoUrl()));
            setData();
            disableSign();
        }
    }

    private void signIn() {
        Intent signInIntent = mSignInClient.getSignInIntent();
        mStartForResult.launch(signInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            mDataAuthSource.createItem(new DataAuth(account.getEmail(), account.getDisplayName(), account.getPhotoUrl()));
            setData();
            disableSign();
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void enableSign() {
        mBtnSignIn.setEnabled(true);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void disableSign() {
        mBtnSignIn.setEnabled(false);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true).replace(R.id.list_of_notes_container, new ListOfNotesFragment()).commit();
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void logOut() {
        mSignInClient.signOut().addOnCompleteListener(task -> enableSign());
    }

    private void setData() {
        DataAuth dataAuth = mDataAuthSource.getDataAuth().get(0);
        NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView userName = headerView.findViewById(R.id.user_name);
        TextView userEmail = headerView.findViewById(R.id.user_email);
        AppCompatImageView imageView = headerView.findViewById(R.id.image_avatar);
        if (dataAuth.getImageProfile() != null) {
            Picasso.get().load(dataAuth.getImageProfile()).into(imageView);
        }
        userName.setText(dataAuth.getFullName());
        userEmail.setText(dataAuth.getEmail());
    }

}