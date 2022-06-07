package com.tez.smartnotepad.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.databinding.FragmentProfileBinding
import com.tez.smartnotepad.ui.fragment.login.LoginFragment
import com.tez.smartnotepad.util.ext.showMessage
import com.tez.smartnotepad.util.ext.sizeAsString
import com.tez.smartnotepad.util.ext.textAsString
import com.tez.smartnotepad.vm.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {


    @Inject
    lateinit var sharedPreferences: PrefDataSource
    private lateinit var user: UserModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    val profileViewModel: ProfileViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences.user?.let {
            this.user = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            tvProfileName.text = user.nameSurname
            etProfileName.setText(user.nameSurname)
            etProfileMail.setText(user.mail)
            etProfilePassword.setText(user.password.toString())

            tvMyNotesCount.text = user.myNotes?.sizeAsString()
            tvSharedNotesCount.text = user.sharedNotes?.sizeAsString()

            btnProfileUpdate.setOnClickListener {
                profileViewModel.updateUser(
                    user.copy
                        (
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}