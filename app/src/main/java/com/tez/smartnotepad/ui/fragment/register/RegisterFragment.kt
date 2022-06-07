package com.tez.smartnotepad.ui.fragment.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.databinding.FragmentRegisterBinding
import com.tez.smartnotepad.ui.fragment.login.LoginFragment
import com.tez.smartnotepad.util.ext.showMessage
import com.tez.smartnotepad.vm.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    val registerViewModel: RegisterViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){

            registerButton.setOnClickListener {
                if (checkInputs()) {
                    val user = UserModel(
                        "0",
                        emailSubstituteWrap.text.toString(),
                        passwordSubstituteCount.text.toString(),
                        nameSubstituteWrap.text.toString(),
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
        }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
