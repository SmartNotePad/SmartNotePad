package com.tez.smartnotepad.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.tez.smartnotepad.R
import com.tez.smartnotepad.network.api.ApiClient
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.datasource.remote.AuthRemoteDataSource
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.AuthRepository
import com.tez.smartnotepad.network.service.UserService
import com.tez.smartnotepad.ui.home.HomeFragment
import com.tez.smartnotepad.ui.register.RegisterFragment
import com.tez.smartnotepad.vm.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var prefDataSource: PrefDataSource
    private lateinit var authRemoteDataSource: AuthRemoteDataSource
    private lateinit var userService: UserService
    private lateinit var apiClient: ApiClient
    private lateinit var mail: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var btnSignInFromInLogin: Button
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefDataSource =
            PrefDataSource(requireContext().getSharedPreferences("SMART", Context.MODE_PRIVATE))

        apiClient = ApiClient
        userService = apiClient.getClient().create(UserService::class.java)
        authRemoteDataSource = AuthRemoteDataSource(userService)

        authRepository = AuthRepository(prefDataSource, authRemoteDataSource)
        loginViewModel = LoginViewModel(authRepository)
    }

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
            loginViewModel.login(
                UserModel(
                    "",
                    mail.text.toString(),
                    password.text.toString(),
                    "",
                    null,
                    null
                )
            ) {
                goHomeFragment()
            }
        }

        btnSignInFromInLogin.setOnClickListener {
            goRegisterFragment()
        }
        return view
    }

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