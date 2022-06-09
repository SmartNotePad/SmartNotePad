package com.tez.smartnotepad.ui.fragment.profile

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.tez.smartnotepad.R
import com.tez.smartnotepad.core.BaseFragmentWithViewModel
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.databinding.FragmentProfileBinding
import com.tez.smartnotepad.ui.fragment.login.LoginFragment
import com.tez.smartnotepad.util.ext.sizeAsString
import com.tez.smartnotepad.util.ext.textAsString
import com.tez.smartnotepad.vm.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment :
    BaseFragmentWithViewModel<FragmentProfileBinding, ProfileViewModel>(
        FragmentProfileBinding::inflate
    ) {

    @Inject
    lateinit var sharedPreferences: PrefDataSource
    private lateinit var user: UserModel

    override val viewModel: ProfileViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences.user?.let {
            this.user = it
        }
    }


    override fun initContentsOfViews() {
        with(binding) {
            tvProfileName.text = user.nameSurname
            etProfileName.setText(user.nameSurname)
            etProfileMail.setText(user.mail)
            etProfilePassword.setText(user.password.toString())

            tvMyNotesCount.text = user.myNotes?.sizeAsString()
            tvSharedNotesCount.text = user.sharedNotes?.sizeAsString()
        }
    }

    override fun initListener() {
        with(binding) {
            btnProfileUpdate.setOnClickListener {
                viewModel.updateUser(
                    user.copy(
                        nameSurname = etProfileName.textAsString(),
                        mail = etProfileName.textAsString(),
                        password = etProfilePassword.textAsString()
                    ), {
                        showMessage("Güncellendi.")
                    }, {
                        showMessage(it)
                    })
            }

            btnLogout.setOnClickListener {
                sharedPreferences.removeUser()
                goLoginFragment()
            }
        }
    }

    private fun goLoginFragment() {
        val loginFragment = LoginFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, loginFragment)
        transaction.commit()
    }
}