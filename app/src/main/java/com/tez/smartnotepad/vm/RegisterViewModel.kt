package com.tez.smartnotepad.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.AuthRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository): ViewModel() {

    fun register(userModel: UserModel) {
        makeNetworkRequest(
            requestFunc = { authRepository.register(userModel) },
            onSuccess = {
                authRepository.saveUserIdToPref(it.userId!!.toInt())
            }
        )
    }

    private fun <T> makeNetworkRequest(
        requestFunc: suspend () -> ResultWrapper<T>,
        onSuccess: ((value: T) -> Unit)? = null,
        onError: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            when (val response = requestFunc.invoke()) {
                is ResultWrapper.Error -> {
                    onError?.invoke()
/*                    hideLoading()
                    handleError(response.value)*/
                    Log.e(RegisterViewModel::class.java.simpleName,"Hata !!")
                    Log.e(RegisterViewModel::class.java.simpleName,response.value.toString())
                }
                is ResultWrapper.Success -> {
                    Log.e(RegisterViewModel::class.java.simpleName,"Başarılı !!")
                    Log.e(RegisterViewModel::class.java.simpleName,response.value.toString())
/*                    hideLoading()
                    onSuccess?.invoke(response.value)*/
                }
            }
        }
    }
}

