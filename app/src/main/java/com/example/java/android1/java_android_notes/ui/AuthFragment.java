package com.example.java.android1.java_android_notes.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.java.android1.java_android_notes.MainActivity;
import com.example.java.android1.java_android_notes.R;
import com.example.java.android1.java_android_notes.data.DataAuth;
import com.example.java.android1.java_android_notes.data.DataAuthSource;
import com.example.java.android1.java_android_notes.data.DataAuthSourceImpl;
import com.example.java.android1.java_android_notes.service.Navigation;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;


public class AuthFragment extends Fragment {

    private static final String TAG = "GoogleAuth";

    private GoogleSignInClient mSignInClient;
    private SignInButton mBtnSignIn;
    private DataAuthSource mDataAuthSource;
    private DrawerLayout mDrawerLayout;
    private final ActivityResultLauncher<Intent> mStartForResultGoogle = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            AuthFragment.this.handleSignInResult(task);
        }
    });
    private DataAuth mDataAuth;
    private CallbackManager mCallbackManager;
    private LoginButton mLoginButton;
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);
        initView(view);
        mDataAuthSource = DataAuthSourceImpl.getInstance();
        initFacebookSign(view);
        initGoogleSign();
        enableSign();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (MainActivity.IS_LOG_OUT) {
            logOutGoogle();
            logOutFacebook(isLoggedIn);
            MainActivity.IS_LOG_OUT = false;
        }

        mLoginButton.setOnClickListener((v) -> LoginManager.getInstance().logInWithReadPermissions(
                requireActivity(),
                mCallbackManager,
                Collections.singletonList("public_profile")));

        return view;
    }

    private void initFacebookSign(View view) {
        mLoginButton = view.findViewById(R.id.login_button);
        mLoginButton.setPermissions(Arrays.asList("public_profile", "email"));
        mLoginButton.setFragment(this);
        registerCallBack();
    }

    private void registerCallBack() {
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                disableSign();
                saveFacebookDataAndShareIt(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(@NonNull FacebookException exception) {
            }
        });
    }

    private void saveFacebookDataAndShareIt(AccessToken token) {
        mDataAuthSource.clear();
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                (object, response) -> {
                    try {
                        String name = Objects.requireNonNull(object).getString("name");
                        String email = object.getString("email");
                        String imageUrl = object.getJSONObject("picture").getJSONObject("data").
                                getString("url");
                        mDataAuthSource.createItem(new DataAuth(email, name, Uri.parse(imageUrl)));
                        mDataAuth = mDataAuthSource.getDataAuth().get(0);
                        MainActivity activity = (MainActivity) mContext;
                        activity.setDataAuth(mDataAuth);
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void initGoogleSign() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);
    }

    private void initView(View view) {
        mBtnSignIn = view.findViewById(R.id.btn_sign_in);
        mBtnSignIn.setOnClickListener((v) -> signInWithGoogle());
        mDrawerLayout = requireActivity().findViewById(R.id.drawer_layout);
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (account != null) {
            saveGoogleDataAndShare(account);
            disableSign();
        }
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            disableSign();
            saveFacebookDataAndShareIt(accessToken);
        }
    }

    private void saveGoogleDataAndShare(GoogleSignInAccount account) {
        mDataAuthSource.clear();
        mDataAuthSource.createItem(new DataAuth(account.getEmail(), account.getDisplayName(), account.getPhotoUrl()));
        mDataAuth = mDataAuthSource.getDataAuth().get(0);
        MainActivity activity = (MainActivity) requireActivity();
        activity.setDataAuth(mDataAuth);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mSignInClient.getSignInIntent();
        mStartForResultGoogle.launch(signInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            saveGoogleDataAndShare(account);
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
        navigation.addFragment(new ListOfNotesFragment(), false);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void logOutGoogle() {
        mSignInClient.signOut().addOnCompleteListener(task -> enableSign());
    }

    private void logOutFacebook(boolean isLoggedIn) {
        if (isLoggedIn) {
            LoginManager.getInstance().logOut();
            enableSign();
        }
    }

}