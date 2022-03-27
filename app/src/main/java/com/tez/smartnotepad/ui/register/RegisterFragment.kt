package com.tez.smartnotepad.ui.register

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.api.ApiClient
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.datasource.remote.AuthRemoteDataSource
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.AuthRepository
import com.tez.smartnotepad.network.service.UserService
import com.tez.smartnotepad.vm.RegisterViewModel

class RegisterFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var prefDataSource: PrefDataSource
    private lateinit var authRemoteDataSource: AuthRemoteDataSource
    private lateinit var userService: UserService
    private lateinit var apiClient: ApiClient

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
        // Inflate the layout for this fragment

        registerViewModel.register(UserModel("0","Android","Android","Android"))


         return inflater.inflate(R.layout.fragment_register, container, false)
    }
}