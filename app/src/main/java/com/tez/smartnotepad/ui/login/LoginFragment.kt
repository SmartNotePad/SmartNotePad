package com.tez.smartnotepad.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.api.ApiClient
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.datasource.remote.AuthRemoteDataSource
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.AuthRepository
import com.tez.smartnotepad.network.service.UserService
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
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefDataSource = PrefDataSource(requireContext().getSharedPreferences("SMART", Context.MODE_PRIVATE))

        apiClient = ApiClient
        userService = apiClient.getClient().create(UserService::class.java)
        authRemoteDataSource = AuthRemoteDataSource(userService)

        authRepository = AuthRepository(prefDataSource,authRemoteDataSource)
        loginViewModel = LoginViewModel(authRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        mail = view.findViewById(R.id.emailSubstituteWrap)
        password = view.findViewById(R.id.etSubstituteCount)
        btnLogin = view.findViewById(R.id.loginButton)

        btnLogin.setOnClickListener {
            loginViewModel.login(UserModel("",mail.text.toString(),password.text.toString(),""))
        }
        return view
    }

}