package com.example.java.android1.java_android_notes.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.java.android1.java_android_notes.MainActivity;
import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.data.DataAuth;
import com.example.java.android1.java_android_notes.data.DataAuthSource;
import com.example.java.android1.java_android_notes.data.DataAuthSourceImpl;
import com.example.java.android1.java_android_notes.service.Navigation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


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
    private DataAuth mDataAuth;

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
            mDataAuth = mDataAuthSource.getDataAuth().get(0);
            MainActivity activity = (MainActivity) requireActivity();
            activity.setDataAuth(mDataAuth);
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
            mDataAuth = mDataAuthSource.getDataAuth().get(0);
            MainActivity activity = (MainActivity) requireActivity();
            activity.setDataAuth(mDataAuth);
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
        Navigation navigation = new Navigation(requireActivity().getSupportFragmentManager(),
                (MainActivity) requireActivity());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(this).commit();
            fragmentManager.beginTransaction().setReorderingAllowed(true).
                    replace(R.id.list_of_notes_container, new ListOfNotesFragment()).commit();
        } else {
            navigation.addFragment(new ListOfNotesFragment(), false, false, false);
        }
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void logOut() {
        mSignInClient.signOut().addOnCompleteListener(task -> enableSign());
    }

}