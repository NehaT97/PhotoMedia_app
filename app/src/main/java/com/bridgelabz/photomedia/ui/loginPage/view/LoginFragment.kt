package com.bridgelabz.photomedia.ui.loginPage.view

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bridgelabz.photomedia.R
import com.bridgelabz.photomedia.ui.homePage.view.HomeDashboardFragment
import com.bridgelabz.photomedia.ui.loginPage.viewmodel.LoginViewModel
import com.bridgelabz.photomedia.ui.registerPage.view.RegisterFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser


class LoginFragment : Fragment() {
    var loginViewModel: LoginViewModel? = null
    var registerAccountTextView: TextView? = null
    var loginButton: Button? = null
    var loginEmailAddress: EditText? = null
    var loginPassword: EditText? = null
    var googleLoginButton:SignInButton  ? = null
    private lateinit var googleSignInClient: GoogleSignInClient
    val RC_SIGN_IN: Int = 123
    var token:String? = null
    private var loggedInUser: FirebaseUser? = null

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configurationGoogleSignIn()
    }

    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val sdkVersion = android.os.Build.VERSION.SDK_INT
        if (sdkVersion > 8) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            findingViewContent(view)
            setInitialViewListeners()
        }
        return view
    }

    private fun configurationGoogleSignIn() {
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            googleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!

    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel?.loginSuccessFull?.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe
            when (it) {
                true -> {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                }
                false -> {
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginViewModel?.loggedAuthUser?.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            } else {
                loggedInUser = it
                val homeDashboardFragment = HomeDashboardFragment()
                val bundle = Bundle()
                bundle.putString("userId", it.uid)
                Log.i("UID", it.uid)
                homeDashboardFragment.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.activity_main_nav_host_fragment, homeDashboardFragment).commit()
                Log.i("User Details[loginFragment]", "${loggedInUser?.email}")
            }
        }
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)!!
            token = account.idToken
            Log.i("token","$token")
        }
    }

    private fun findingViewContent(view: View?) {
        if (view != null) {
            registerAccountTextView = view.findViewById(R.id.createAccount)
            loginEmailAddress = view.findViewById(R.id.loginEmailAddress)
            loginPassword = view.findViewById(R.id.loginPassword)
            loginButton = view.findViewById(R.id.loginButton)
            googleLoginButton= view.findViewById(R.id.googleLoginButton)
        }
    }

    private fun setInitialViewListeners() {
        setLoginButtonListeners()
        setRegisterTextViewListeners()
        setGoogleLoginButtonListener()
    }

    private fun setGoogleLoginButtonListener(){
        googleLoginButton?.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
            token?.let { it1 -> loginViewModel?.loginUserByGoogle(it1) }
            Toast.makeText(context, "Navigating To HomePage", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setRegisterTextViewListeners() {
        registerAccountTextView?.setOnClickListener {
            Toast.makeText(context, "Navigating To Register", Toast.LENGTH_SHORT).show()
             val registerFragment = RegisterFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.activity_main_nav_host_fragment,
                registerFragment).addToBackStack("registerFragment").commit()
        }
    }

    private fun setLoginButtonListeners() {
        loginButton?.setOnClickListener {
            val emailId = loginEmailAddress?.text.toString()
            val password = loginPassword?.text.toString()
            if (emailId.isEmpty()) {
                Toast.makeText(context, "Please enter emailId", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (password.isEmpty()) {
                Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginViewModel?.loginUser(emailId, password)
        }
    }

}