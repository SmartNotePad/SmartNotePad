package com.tez.smartnotepad.ui.fragment.login

import androidx.fragment.app.viewModels
import com.tez.smartnotepad.R
import com.tez.smartnotepad.core.BaseFragmentWithViewModel
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.databinding.FragmentLoginBinding
import com.tez.smartnotepad.ui.fragment.home.HomeFragment
import com.tez.smartnotepad.ui.fragment.register.RegisterFragment
import com.tez.smartnotepad.vm.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment :
    BaseFragmentWithViewModel<FragmentLoginBinding, LoginViewModel>(
        FragmentLoginBinding::inflate
    ) {

    @Inject
    lateinit var preferences: PrefDataSource
    override val viewModel: LoginViewModel by viewModels()

    private lateinit var user: UserModel

    override fun initListener() {
        with(binding) {
            loginButton.setOnClickListener {
                user = getUserFromInputs()

                viewModel.login(
                    user
                ) {
                    preferences.user = it
                    goHomeFragment()
                }
            }

            btnSignUpFromInLogin.setOnClickListener {
                goRegisterFragment()
            }
        }
    }

    private fun getUserFromInputs(): UserModel =
        with(binding) {
            UserModel(
                "",
                emailSubstituteWrap.text.toString(),
                etSubstituteCount.text.toString(),
                "",
                null,
                null
            )
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