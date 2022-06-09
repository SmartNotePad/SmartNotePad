package com.tez.smartnotepad.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.core.BaseViewModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.AuthRepository
import com.tez.smartnotepad.network.helper.Request.makeNetworkRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val authRepositoryImpl: AuthRepository) : BaseViewModel() {

    private lateinit var _user: UserModel
    val user: UserModel
        get() = _user

    fun login(userModel: UserModel, onSuccess: (user:UserModel) -> Unit) {
        makeNetworkRequest(
            requestFunc = { authRepositoryImpl.login(userModel) },
            onSuccess = {
                onSuccess.invoke(it)
            }, {}, viewModelScope
        )
    }
}