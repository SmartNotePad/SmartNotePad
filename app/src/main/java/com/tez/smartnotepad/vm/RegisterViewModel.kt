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

class RegisterViewModel(private val authRepository: AuthRepository): ViewModel() {


    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean>
        get() = _registerResult

    fun register(userModel: UserModel) {
        makeNetworkRequest(
            requestFunc = { authRepository.register(userModel) }
        )
    }

    private fun <T> makeNetworkRequest(
        requestFunc: suspend () -> ResultWrapper<T>
    ) {
        viewModelScope.launch {
            when (val response = requestFunc.invoke()) {
                is ResultWrapper.Success -> {
                    _registerResult.value = true
                }
                is ResultWrapper.Error -> {
                    // TODO handle error
                    Log.e(RegisterViewModel::class.java.simpleName,"Yeni üye Hata !!")
                    Log.e(RegisterViewModel::class.java.simpleName,response.value)
                    _registerResult.value = false
                }
            }
        }
    }
}

