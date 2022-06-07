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
import com.tez.smartnotepad.databinding.FragmentLoginBinding
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

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var user: UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){

            loginButton.setOnClickListener {
                user = getUserFromInputs()

                loginViewModel.login(
                    user
                ) {
                    Log.e(name(),user.toString())
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

        with(binding){
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}