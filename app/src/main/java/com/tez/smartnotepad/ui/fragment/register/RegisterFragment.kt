package com.tez.smartnotepad.ui.fragment.register

import androidx.fragment.app.viewModels
import com.tez.smartnotepad.R
import com.tez.smartnotepad.core.BaseFragmentWithViewModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.databinding.FragmentRegisterBinding
import com.tez.smartnotepad.ui.fragment.login.LoginFragment
import com.tez.smartnotepad.vm.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment :
    BaseFragmentWithViewModel<FragmentRegisterBinding, RegisterViewModel>(
        FragmentRegisterBinding::inflate
    ) {

    override val viewModel: RegisterViewModel by viewModels()

    override fun initListener() {
        with(binding) {
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

                    viewModel.register(user, {
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
}
