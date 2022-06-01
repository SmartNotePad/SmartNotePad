package com.tez.smartnotepad.ui.fragment.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.ui.fragment.home.HomeFragment
import com.tez.smartnotepad.ui.fragment.register.RegisterFragment
import com.tez.smartnotepad.util.ext.name
import com.tez.smartnotepad.vm.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var preferences: PrefDataSource
    val loginViewModel: LoginViewModel by viewModels()

    private lateinit var user: UserModel
    private lateinit var mail: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var btnSignInFromInLogin: Button
    private lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        mail = view.findViewById(R.id.emailSubstituteWrap)
        password = view.findViewById(R.id.etSubstituteCount)
        btnSignInFromInLogin = view.findViewById(R.id.btnSignUpFromInLogin)
        btnLogin = view.findViewById(R.id.loginButton)

        btnLogin.setOnClickListener {
            user = getUserFromInputs()

            loginViewModel.login(
                user
            ) {
                Log.e(name(),user.toString())
                preferences.user = it
                goHomeFragment()
            }
        }

        btnSignInFromInLogin.setOnClickListener {
            goRegisterFragment()
        }
        return view
    }

    private fun getUserFromInputs(): UserModel =
        UserModel(
            "",
            mail.text.toString(),
            password.text.toString(),
            "",
            null,
            null
        )

    private fun goHomeFragment() {
        val homeFragment = HomeFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, homeFragment)
        transaction.commit()
    }

    private fun goRegisterFragment() {
        val registerFragment = RegisterFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, registerFragment)
        transaction.commit()
    }

}