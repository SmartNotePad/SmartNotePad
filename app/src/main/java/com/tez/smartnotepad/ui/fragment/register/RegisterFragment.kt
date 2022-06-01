package com.tez.smartnotepad.ui.fragment.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.ui.fragment.login.LoginFragment
import com.tez.smartnotepad.util.ext.showMessage
import com.tez.smartnotepad.vm.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    val registerViewModel: RegisterViewModel by viewModels()

    private lateinit var nameSurname: TextInputEditText
    private lateinit var mail: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var passwordAgain: TextInputEditText

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
            if (checkInputs()) {
                val user = UserModel(
                    "0",
                    mail.text.toString(),
                    password.text.toString(),
                    nameSurname.text.toString(),
                    null,
                    null
                )
                registerViewModel.register(user, {
                    goLoginFragment()
                    showMessage("Giriş yapabilirsiniz.")
                }, {
                    showMessage("Yeni üye oluşturulamadı. Mail kullanılıyor.")
                })
            }
        }

        btnSignInFromInRegister.setOnClickListener {
            goLoginFragment()
        }
        return view
    }

    private fun checkInputs(): Boolean {
        return true
    }

    private fun goLoginFragment() {
        val loginFragment = LoginFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, loginFragment)
        transaction.commit()
    }
}
