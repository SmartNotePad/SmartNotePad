package com.tez.smartnotepad.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository): ViewModel() {

    private lateinit var _user: UserModel
    val user: UserModel
        get()  = _user

    fun login(userModel: UserModel) {
        makeNetworkRequest(
            requestFunc = { authRepository.login(userModel) }
        )
    }
    private fun <T> makeNetworkRequest(
        requestFunc: suspend () -> ResultWrapper<T>
    ) {
        viewModelScope.launch {
            when (val response = requestFunc.invoke()) {
                is ResultWrapper.Success -> {
                    authRepository.saveUserToPref((response.value as UserModel))
                }
                is ResultWrapper.Error -> {
                    // TODO error handle
                    Log.e(LoginViewModel::class.java.simpleName,"Giriş Hata !!")
                    Log.e(LoginViewModel::class.java.simpleName,response.value.toString())
                }
            }
        }
    }
}