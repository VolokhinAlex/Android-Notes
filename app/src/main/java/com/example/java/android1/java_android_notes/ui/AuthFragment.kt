package com.example.java.android1.java_android_notes.ui

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.java.android1.java_android_notes.MainActivity
import com.example.java.android1.java_android_notes.R
import com.example.java.android1.java_android_notes.data.DataAuth
import com.example.java.android1.java_android_notes.data.DataAuthSource
import com.example.java.android1.java_android_notes.data.DataAuthSourceImpl
import com.example.java.android1.java_android_notes.service.Navigation
import com.facebook.*
import com.facebook.CallbackManager.Factory.create
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.json.JSONException

class AuthFragment : Fragment() {
    private var signInClient: GoogleSignInClient? = null
    private var btnSignIn: SignInButton? = null
    private var dataAuthSource: DataAuthSource? = null
    private var drawerLayout: DrawerLayout? = null
    private val startForResultGoogle =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            }
        }
    private var dataAuth: DataAuth? = null
    private var callbackManager: CallbackManager? = null
    private var loginButton: LoginButton? = null
    private var _context: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbackManager = create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_auth, container, false)
        initView(view)
        dataAuthSource = DataAuthSourceImpl.instance
        initFacebookSign(view)
        initGoogleSign()
        enableSign()
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if (MainActivity.IS_LOG_OUT) {
            logOutGoogle()
            logOutFacebook(isLoggedIn)
            MainActivity.IS_LOG_OUT = false
        }
        loginButton?.setOnClickListener {
            callbackManager?.let {
                LoginManager.getInstance().logInWithReadPermissions(
                    requireActivity(),
                    it, listOf("public_profile")
                )
            }
        }
        return view
    }

    private fun initFacebookSign(view: View) {
        loginButton = view.findViewById(R.id.login_button)
        loginButton?.permissions = listOf("public_profile", "email")
        loginButton?.setFragment(this)
        registerCallBack()
    }

    private fun registerCallBack() {
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onCancel() {}
                override fun onError(error: FacebookException) {}
                override fun onSuccess(result: LoginResult) {
                    disableSign()
                    saveFacebookDataAndShareIt(result.accessToken)
                }

            })
    }

    private fun saveFacebookDataAndShareIt(token: AccessToken?) {
        dataAuthSource?.clear()
        val request = GraphRequest.newMeRequest(
            token
        ) { jsonObject, _ ->
            try {
                val name = jsonObject?.getString("name")
                val email = jsonObject?.getString("email")
                val imageUrl =
                    jsonObject?.getJSONObject("picture")?.getJSONObject("data")?.getString("url")
                dataAuthSource?.createItem(DataAuth(email, name, Uri.parse(imageUrl)))
                dataAuth = dataAuthSource?.dataAuth?.get(0)
                val activity = _context as MainActivity?
                activity?.setDataAuth(dataAuth)
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,link,email,picture.type(large)")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun initGoogleSign() {
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        signInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
    }

    private fun initView(view: View) {
        btnSignIn = view.findViewById(R.id.btn_sign_in)
        btnSignIn?.let {
            it.setOnClickListener { signInWithGoogle() }
        }
        drawerLayout = requireActivity().findViewById(R.id.drawer_layout)
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account != null) {
            saveGoogleDataAndShare(account)
            disableSign()
        }
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if (isLoggedIn) {
            disableSign()
            saveFacebookDataAndShareIt(accessToken)
        }
    }

    private fun saveGoogleDataAndShare(account: GoogleSignInAccount) {
        dataAuthSource?.clear()
        dataAuthSource?.createItem(DataAuth(account.email, account.displayName, account.photoUrl))
        dataAuth = dataAuthSource?.dataAuth?.get(0)
        val activity = requireActivity() as MainActivity
        activity.setDataAuth(dataAuth)
    }

    private fun signInWithGoogle() {
        val signInIntent = signInClient?.signInIntent
        startForResultGoogle.launch(signInIntent)
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            saveGoogleDataAndShare(account)
            disableSign()
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun enableSign() {
        btnSignIn?.isEnabled = true
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun disableSign() {
        btnSignIn?.isEnabled = false
        val navigation = Navigation(
            requireActivity().supportFragmentManager,
            (requireActivity() as MainActivity)
        )
        navigation.addFragment(ListOfNotesFragment(), false)
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    private fun logOutGoogle() {
        signInClient?.signOut()?.addOnCompleteListener { enableSign() }
    }

    private fun logOutFacebook(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            LoginManager.getInstance().logOut()
            enableSign()
        }
    }

    companion object {
        private const val TAG = "GoogleAuth"
    }
}