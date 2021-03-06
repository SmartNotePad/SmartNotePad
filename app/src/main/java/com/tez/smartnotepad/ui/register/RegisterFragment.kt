package com.tez.smartnotepad.ui.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.tez.smartnotepad.R
import com.tez.smartnotepad.network.api.ApiClient
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.datasource.remote.AuthRemoteDataSource
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.AuthRepository
import com.tez.smartnotepad.network.service.UserService
import com.tez.smartnotepad.ui.login.LoginFragment
import com.tez.smartnotepad.vm.RegisterViewModel

class RegisterFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var prefDataSource: PrefDataSource
    private lateinit var authRemoteDataSource: AuthRemoteDataSource
    private lateinit var userService: UserService
    private lateinit var apiClient: ApiClient
    private lateinit var nameSurname: TextInputEditText
    private lateinit var mail: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var passwordAgain: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefDataSource = PrefDataSource(requireContext().getSharedPreferences("SMART",Context.MODE_PRIVATE))

        apiClient = ApiClient
        userService = apiClient.getClient().create(UserService::class.java)
        authRemoteDataSource = AuthRemoteDataSource(userService)

        authRepository = AuthRepository(prefDataSource,authRemoteDataSource)
        registerViewModel = RegisterViewModel(authRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        nameSurname = view.findViewById(R.id.nameSubstituteWrap)
        mail = view.findViewById(R.id.emailSubstituteWrap)
        password = view.findViewById(R.id.passwordSubstituteCount)
        passwordAgain = view.findViewById(R.id.passwordCheckSubstituteCount)

        val btnRegister = view.findViewById<Button>(R.id.registerButton)
        val btnSignInFromInRegister = view.findViewById<Button>(R.id.btnSignInFromInRegister)

        btnRegister.setOnClickListener {
            if (check()){
                val user = UserModel("0",mail.text.toString(),password.text.toString(),nameSurname.text.toString(),null,null)
                registerViewModel.register(user)
            }
        }

        btnSignInFromInRegister.setOnClickListener {
            goLoginFragment()
        }

        registerViewModel.registerResult.observe(viewLifecycleOwner) {
            if (it){
                goLoginFragment()
                showMessage("Giri?? yapabilirsiniz.")
            }else{
                showMessage("Yeni ??ye olu??turulamad??. Mail kullan??l??yor.")
            }
        }
        return view
    }


    private fun check(): Boolean {
        return true
    }
    private fun goLoginFragment() {
        val loginFragment = LoginFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, loginFragment)
        transaction.commit()
    }
    private fun showMessage(message:String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}
