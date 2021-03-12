package com.bridgelabz.photomedia.ui.register.view

import android.os.Bundle
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
import com.bridgelabz.photomedia.data.model.RegisterDTO
import com.bridgelabz.photomedia.ui.login.view.LoginFragment
import com.bridgelabz.photomedia.ui.register.viewmodel.RegisterViewModel


class RegisterFragment : Fragment() {

    private var registerViewModel: RegisterViewModel? = null
    private var firstNameTextView: EditText? = null
    private var lastNameTextView: EditText? = null
    private var usernameTextView: EditText? = null
    private var passWordTextView: EditText? = null
    private var emailTextView: EditText? = null
    private var signUpButton: Button? = null
    private var gotoLoginPage: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        initiateViewContents(view)
        initiateViewListeners()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        registerViewModel?.registerSuccessFul?.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe
            if (it) {
                Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                val loginFragment = LoginFragment()
                requireActivity().supportFragmentManager.beginTransaction().replace(
                    R.id.activity_main_nav_host_fragment,loginFragment).commit()
                return@observe
            }
            Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initiateViewContents(view: View?) {
        firstNameTextView = view?.findViewById<EditText>(R.id.userName)
        lastNameTextView = view?.findViewById<EditText>(R.id.firstName)
        passWordTextView = view?.findViewById<EditText>(R.id.registerPassword)
        emailTextView = view?.findViewById<EditText>(R.id.registerEmail)
        signUpButton = view?.findViewById<Button>(R.id.signUpButton)
        gotoLoginPage = view?.findViewById<TextView>(R.id.registerTextView)
    }

    private fun initiateViewListeners() {
        signUpButtonClickListener()
        goToLoginPageTextViewListener()
    }

    private fun signUpButtonClickListener() {
        signUpButton?.setOnClickListener {
            val firstName = firstNameTextView?.text.toString()
            val lastName = lastNameTextView?.text.toString()
            val emailId = emailTextView?.text.toString()
            val password = passWordTextView?.text.toString()

            val registerDTO = RegisterDTO(firstName, lastName, emailId, emailId, password)
            registerViewModel?.registerUser(registerDTO)
        }
    }

    private fun goToLoginPageTextViewListener() {
        gotoLoginPage?.setOnClickListener {
            Toast.makeText(context, "Navigating To Login Page", Toast.LENGTH_SHORT).show()
            val loginFragment = LoginFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.activity_main_nav_host_fragment,loginFragment).commit()

        }
    }
}